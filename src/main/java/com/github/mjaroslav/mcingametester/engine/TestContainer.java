package com.github.mjaroslav.mcingametester.engine;

import cpw.mods.fml.common.ClassNameUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Getter
public class TestContainer {
    protected final @NotNull Class<?> testClass;

    protected final @Nullable Method beforeClassMethod;
    protected final @Nullable Method afterClassMethod;
    protected final @Nullable Method beforeEachMethod;
    protected final @Nullable Method afterEachMethod;

    protected final @NotNull Object object;
    protected final @NotNull Set<TestTask> testTasks = new HashSet<>();

    public void addTest(@NotNull TestTask testTask) {
        testTasks.add(testTask);
    }

    public @NotNull String getContainerName() {
        return ClassNameUtils.shortName(testClass);
    }

    public @NotNull TestState getState() {
        return getTestTasks().stream().map(TestTask::getState).max(Comparator.comparingInt(Enum::ordinal))
                .orElse(TestState.AWAITING);
    }

    public int getTestCount() {
        return getTestTasks().size();
    }

    public boolean isHaveNoTests() {
        return getTestTasks().isEmpty();
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
        return "TestContainer@" + getContainerName();
    }
}
