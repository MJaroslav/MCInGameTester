package com.github.mjaroslav.mcingametester.loader;

import com.github.mjaroslav.mcingametester.api.*;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;

@UtilityClass
public class TestFactory {
    public @NotNull TestContainer buildTestContainerFromClassName(@NotNull String className) throws Exception {
        val testClass = Class.forName(className);
        Method beforeClassMethod = null;
        Method afterClassMethod = null;
        Method beforeEachMethod = null;
        Method afterEachMethod = null;
        val testMethods = new HashSet<Method>();
        for (var method : testClass.getMethods()) {
            val mods = method.getModifiers();
            if (method.isAnnotationPresent(BeforeClass.class)) {
                if (!Modifier.isStatic(mods) || !Modifier.isPublic(mods))
                    throw new IllegalStateException("@BeforeClass method should be public and static");
                if (!method.getReturnType().equals(void.class))
                    throw new IllegalStateException("All annotated methods should be void");
                if (beforeClassMethod == null) beforeClassMethod = method;
                else throw new IllegalStateException("@BeforeClass should be one per class");
            } else if (method.isAnnotationPresent(AfterClass.class)) {
                if (!Modifier.isStatic(mods) || !Modifier.isPublic(mods))
                    throw new IllegalStateException("@AfterClass method should be public and static");
                if (!method.getReturnType().equals(void.class))
                    throw new IllegalStateException("All annotated methods should be void");
                if (afterClassMethod == null) afterClassMethod = method;
                else throw new IllegalStateException("@AfterClass should be one per class");
            } else if (method.isAnnotationPresent(com.github.mjaroslav.mcingametester.api.Test.class)) {
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
                if (beforeEachMethod == null) beforeEachMethod = method;
                else throw new IllegalStateException("@BeforeEach should be one per class");
            } else if (method.isAnnotationPresent(AfterEach.class)) {
                if (!Modifier.isPublic(mods))
                    throw new IllegalStateException("@AfterEach method should be public and static");
                if (!method.getReturnType().equals(void.class))
                    throw new IllegalStateException("All annotated methods should be void");
                if (afterEachMethod == null) afterEachMethod = method;
                else throw new IllegalStateException("@AfterEach should be one per class");
            }
        }
        Field worldField = null;
        for (var field : testClass.getFields())
            if (field.isAnnotationPresent(WorldShadow.class)) {
                val mods = testClass.getModifiers();
                if (!Modifier.isPublic(mods) || Modifier.isStatic(mods))
                    throw new IllegalStateException("@WorldShadow field should be public and non-static");
                if (!field.getType().equals(World.class))
                    throw new IllegalStateException("@WorldShadow field should be net.minecraft.world.World type");
                if (worldField == null)
                    if (testClass.isAnnotationPresent(Server.class)) worldField = field;
                    else throw new IllegalStateException("@WorldShadow can be only in @Server test classes");
                else throw new IllegalStateException("@WorldShadow should be one per class");
            }
        val object = testClass.getConstructor().newInstance();
        if (worldField != null) worldField.set(object, MinecraftServer.getServer().worldServers[0]);
        val result = new TestContainer(testClass, beforeClassMethod, afterClassMethod, beforeEachMethod,
                afterEachMethod, object);
        for (var testMethod : testMethods) {
            val expectedException =
                    testMethod.getAnnotation(com.github.mjaroslav.mcingametester.api.Test.class).value();
            if (!expectedException.equals(void.class) && !expectedException.isAssignableFrom(Throwable.class))
                throw new IllegalStateException("Excepted exception should be with void or Throwable type");
            val test = new Test(result, expectedException, testMethod, object);
            result.addTest(test);
        }
        return result;
    }
}
