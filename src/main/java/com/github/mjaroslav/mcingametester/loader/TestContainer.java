package com.github.mjaroslav.mcingametester.loader;

import com.github.mjaroslav.mcingametester.MCInGameTester;
import com.github.mjaroslav.mcingametester.api.*;
import com.github.mjaroslav.mcingametester.lib.ModInfo;
import cpw.mods.fml.common.ClassNameUtils;
import cpw.mods.fml.common.FMLCommonHandler;
import lombok.val;
import net.minecraft.crash.CrashReport;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class TestContainer {
    private @Nullable Method beforeClassMethod = null;
    private @Nullable Method afterClassMethod = null;

    private @Nullable Method beforeEachMethod = null;
    private @Nullable Method afterEachMethod = null;

    private final @NotNull Set<Method> testMethods = new HashSet<>();

    private final @NotNull Object object;
    private final @NotNull Class<?> testClass;

    public TestContainer(@NotNull String className) {
        try {
            testClass = Class.forName(className);
        } catch (ClassNotFoundException e) { // Impossible
            throw new RuntimeException(e); // For final field
        }
        for (var method : testClass.getMethods()) {
            val mods = method.getModifiers();
            if (method.isAnnotationPresent(BeforeClass.class)) {
                if (!Modifier.isStatic(mods) || !Modifier.isPublic(mods))
                    throw new IllegalStateException("@BeforeClass method should be public and static");
                if (!method.getReturnType().equals(void.class))
                    throw new IllegalStateException("All annotated methods should be void");
                if (beforeClassMethod == null)
                    beforeClassMethod = method;
                else throw new IllegalStateException("@BeforeClass should be one per class");
            } else if (method.isAnnotationPresent(AfterClass.class)) {
                if (!Modifier.isStatic(mods) || !Modifier.isPublic(mods))
                    throw new IllegalStateException("@AfterClass method should be public and static");
                if (!method.getReturnType().equals(void.class))
                    throw new IllegalStateException("All annotated methods should be void");
                if (afterClassMethod == null)
                    afterClassMethod = method;
                else throw new IllegalStateException("@AfterClass should be one per class");
            } else if (method.isAnnotationPresent(Test.class)) {
                if (!Modifier.isPublic(mods))
                    throw new IllegalStateException("@Test method should be public and static");
                if (!method.getReturnType().equals(void.class))
                    throw new IllegalStateException("All annotated methods should be void");
                testMethods.add(method);
            } else if (method.isAnnotationPresent(BeforeEach.class)) {
                if (!Modifier.isPublic(mods))
                    throw new IllegalStateException("@BeforeEach method should be public and static");
                if (!method.getReturnType().equals(void.class))
                    throw new IllegalStateException("All annotated methods should be void");
                if (beforeEachMethod == null)
                    beforeEachMethod = method;
                else throw new IllegalStateException("@BeforeEach should be one per class");
            } else if (method.isAnnotationPresent(AfterEach.class)) {
                if (!Modifier.isPublic(mods))
                    throw new IllegalStateException("@AfterEach method should be public and static");
                if (!method.getReturnType().equals(void.class))
                    throw new IllegalStateException("All annotated methods should be void");
                if (afterEachMethod == null)
                    afterEachMethod = method;
                else throw new IllegalStateException("@AfterEach should be one per class");
            }
        }
        Field worldField = null;
        for (var field : testClass.getFields()) {
            if (field.isAnnotationPresent(WorldShadow.class)) {
                val mods = testClass.getModifiers();
                if (!Modifier.isPublic(mods) || Modifier.isStatic(mods))
                    throw new IllegalStateException("@WorldShadow field should be public and non-static");
                if (!field.getType().equals(World.class))
                    throw new IllegalStateException("@WorldShadow field should be net.minecraft.world.World type");
                if (worldField == null)
                    if (testClass.isAnnotationPresent(Server.class))
                        worldField = field;
                    else throw new IllegalStateException("@WorldShadow can be only in @Server test classes");
                else throw new IllegalStateException("@WorldShadow should be one per class");
            }
        }
        try {
            object = testClass.getConstructor().newInstance();
            if (worldField != null)
                worldField.set(object, MinecraftServer.getServer().worldServers[0]);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Test class must have constructor without arguments");
        } catch (InvocationTargetException | IllegalAccessException e) { // Impossible
            throw new RuntimeException(e); // For final field
        } catch (InstantiationException e) {
            throw new IllegalStateException("Test class can't be abstract or something");
        }
    }

    public void runTests() {
        MCInGameTester.proxy.startLogTest(this);
        runMethod(beforeClassMethod, true);

        testMethods.forEach(method -> {
            MCInGameTester.proxy.stepLogTest(method.getName());
            runMethod(beforeEachMethod, false);
            runMethod(method, false);
            runMethod(afterEachMethod, false);
        });

        runMethod(afterClassMethod, true);
        MCInGameTester.proxy.endLogTest();
    }

    public int getTestCount() {
        return testMethods.size();
    }

    public @NotNull String getTestClassName() {
        return ClassNameUtils.shortName(testClass);
    }

    private void runMethod(@Nullable Method method, boolean isStatic) {
        if (method != null)
            try {
                method.invoke(isStatic ? null : object);
            } catch (IllegalAccessException ignored) { // Impossible
            } catch (InvocationTargetException e) {
                if (isStatic)
                    throw new ReportedException(CrashReport.makeCrashReport(e, "Error while test execution"));
                else if (e.getCause() instanceof AssertionError assertError) {
                    ModInfo.LOG.error("Test " + getTestClassName() + "/" + method.getName() +
                            " failed", e.getCause());
                    FMLCommonHandler.instance().exitJava(-1, true); // WTF I don't know why exceptions is ignored on client anyway
                } else
                    ModInfo.LOG.error("Strange exception in " + getTestClassName() + "/" + method.getName(),
                            e.getCause());
            }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TestContainer container && testClass.equals(container.testClass);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(testClass);
    }

    @Override
    public String toString() {
        return "TestContainer@" + testClass;
    }
}
