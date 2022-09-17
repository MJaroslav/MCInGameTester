package com.github.mjaroslav.mcingametester.engine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public final class TestTask {
    private final @NotNull TestContainer container;
    private final @Nullable Class<?> expectedException;
    private final @NotNull Method testMethod;
    private final @NotNull Object testObject;

    private @NotNull TestState state = TestState.AWAITING;
    private @Nullable Throwable result;

    public @NotNull String getName() {
        return testMethod.getName();
    }

    public void setResult(@NotNull TestState state, @Nullable Throwable result) {
        if (state == TestState.AWAITING) throw new IllegalArgumentException("You trying to mark test as AWAITING");
        if (this.state != TestState.AWAITING) throw new IllegalStateException("You trying to start test " + this
                + " twice");
        this.state = state;
        this.result = result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(testMethod);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TestTask testTask && testMethod.equals(testTask.testMethod);
    }

    @Override
    public String toString() {
        return "TestTask@" + container.getContainerName() + "/" + getName();
    }
}
