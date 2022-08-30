package com.github.mjaroslav.mcingametester.loader;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class Test {
    protected final @NotNull TestContainer container;
    protected final @NotNull Class<?> expectedException;
    protected final @NotNull Method testMethod;
    protected final @NotNull Object testObject;

    protected @NotNull TestState state = TestState.UNSTARTED;
    protected @Nullable Throwable result;

    public @NotNull String getName() {
        return testMethod.getName();
    }

    public void setResult(@NotNull TestState state, @Nullable Throwable result) {
        if (state == TestState.UNSTARTED)
            throw new IllegalArgumentException("You trying to mark test unstated");
        if (this.state != TestState.UNSTARTED)
            throw new IllegalStateException("You trying to start test " + this + " twice");
        this.state = state;
        this.result = result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(testMethod);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Test test && testMethod.equals(test.testMethod);
    }

    @Override
    public String toString() {
        return "Test@" + container.getContainerName() + "/" + getName();
    }
}
