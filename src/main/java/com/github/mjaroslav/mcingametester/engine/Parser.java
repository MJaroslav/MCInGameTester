package com.github.mjaroslav.mcingametester.engine;

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
public class Parser {
    public @NotNull TestContainer parseFromClassName(@NotNull String className) throws Exception {
        val testClass = Class.forName(className);
        Method beforeClassMethod = null;
        Method afterClassMethod = null;
        Method beforeEachMethod = null;
        Method afterEachMethod = null;
        val testMethods = new HashSet<Method>();
        for (var method : testClass.getMethods()) {
            val mods = method.getModifiers();
            val notVoid = !method.getReturnType().equals(void.class);
            if (!Modifier.isPublic(mods)) method.setAccessible(true); // Just make all methods public by one line, lol
            if (method.isAnnotationPresent(BeforeClass.class)) {
                if (!Modifier.isStatic(mods)) throw new IllegalStateException("@BeforeClass method should be static");
                if (notVoid) throw new IllegalStateException("All annotated methods should be void");
                if (beforeClassMethod == null) beforeClassMethod = method;
                else throw new IllegalStateException("@BeforeClass should be one per class");
            } else if (method.isAnnotationPresent(AfterClass.class)) {
                if (!Modifier.isStatic(mods)) throw new IllegalStateException("@AfterClass method should be static");
                if (notVoid) throw new IllegalStateException("All annotated methods should be void");
                if (afterClassMethod == null) afterClassMethod = method;
                else throw new IllegalStateException("@AfterClass should be one per class");
            } else if (method.isAnnotationPresent(Test.class)) {
                if (notVoid) throw new IllegalStateException("All annotated methods should be void");
                testMethods.add(method);
            } else if (method.isAnnotationPresent(BeforeEach.class)) {
                if (notVoid) throw new IllegalStateException("All annotated methods should be void");
                if (beforeEachMethod == null) beforeEachMethod = method;
                else throw new IllegalStateException("@BeforeEach should be one per class");
            } else if (method.isAnnotationPresent(AfterEach.class)) {
                if (notVoid) throw new IllegalStateException("All annotated methods should be void");
                if (afterEachMethod == null) afterEachMethod = method;
                else throw new IllegalStateException("@AfterEach should be one per class");
            }
        }
        Field worldField = null;
        for (var field : testClass.getFields())
            if (field.isAnnotationPresent(WorldShadow.class)) {
                val mods = testClass.getModifiers();
                if (Modifier.isStatic(mods)) throw new IllegalStateException("@WorldShadow field should be non-static");
                if (!Modifier.isPublic(mods)) field.setAccessible(true);
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
            var expectedException = testMethod.getAnnotation(Test.class).expected();
            if (expectedException.equals(Test.None.class)) expectedException = null;
            val test = new TestTask(result, expectedException, testMethod, object);
            result.addTest(test);
        }
        return result;
    }
}
