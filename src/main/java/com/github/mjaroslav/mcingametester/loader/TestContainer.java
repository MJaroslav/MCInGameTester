package com.github.mjaroslav.mcingametester.loader;

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
    protected final @NotNull Set<Test> tests = new HashSet<>();

    public void addTest(@NotNull Test test) {
        tests.add(test);
    }

    public @NotNull String getContainerName() {
        return ClassNameUtils.shortName(testClass);
    }

    public @NotNull TestState getState() {
        return getTests().stream().map(Test::getState).max(Comparator.comparingInt(Enum::ordinal))
                .orElse(TestState.UNSTARTED);
    }

    public int getTestCount() {
        return getTests().size();
    }

    public boolean isHaveNoTests() {
        return getTests().isEmpty();
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
