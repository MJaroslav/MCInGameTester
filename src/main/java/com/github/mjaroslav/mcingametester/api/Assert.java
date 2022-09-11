package com.github.mjaroslav.mcingametester.api;

import com.github.mjaroslav.mcingametester.util.ComparisonError;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Use this utility class for test writing helping.
 */
@UtilityClass
public class Assert {
    /**
     * Fail test with (or not) message.
     *
     * @param message Optional fail message.
     */
    public void fail(@Nullable String message) {
        if (message == null) throw new AssertionError();
        else throw new AssertionError(message);
    }

    /**
     * Print difference and fail test with message.
     *
     * @param expected expected comparing object.
     * @param actual   actual comparing object.
     * @param message  Optional message.
     */
    public void fail(@Nullable Object expected, @Nullable Object actual, @Nullable String message) {
        if (message == null) throw new ComparisonError(expected, actual);
        else throw new ComparisonError(message, expected, actual);
    }

    public void isTrue(boolean condition, @Nullable String message) {
        if (!condition) fail(message);
    }

    public void isTrue(boolean condition) {
        isTrue(condition, null);
    }

    public void isFalse(boolean condition, @Nullable String message) {
        if (condition) fail(message);
    }

    public void isFalse(boolean condition) {
        isTrue(condition, null);
    }


    public void isEquals(@Nullable Object expected, @Nullable Object actual, @Nullable String message) {
        if (!Objects.equals(expected, actual)) fail(expected, actual, message);
    }

    public void isEquals(@Nullable Object expected, @Nullable Object actual) {
        isEquals(expected, actual, null);
    }

    public void isEquals(int expected, int actual, @Nullable String message) {
        if (expected != actual) fail(expected, actual, message);
    }

    public void isEquals(int expected, int actual) {
        isEquals(expected, actual, null);
    }

    public void isEquals(boolean expected, boolean actual, @Nullable String message) {
        if (expected != actual) fail(expected, actual, message);
    }

    public void isEquals(boolean expected, boolean actual) {
        isEquals(expected, actual, null);
    }

    public void isEquals(short expected, short actual, @Nullable String message) {
        if (expected != actual) fail(expected, actual, message);
    }

    public void isEquals(short expected, short actual) {
        isEquals(expected, actual, null);
    }

    public void isEquals(byte expected, byte actual, @Nullable String message) {
        if (expected != actual) fail(expected, actual, message);
    }

    public void isEquals(byte expected, byte actual) {
        isEquals(expected, actual, null);
    }

    public void isEquals(char expected, char actual, @Nullable String message) {
        if (expected != actual) fail(expected, actual, message);
    }

    public void isEquals(char expected, char actual) {
        isEquals(expected, actual, null);
    }

    public void isEquals(float expected, float actual, float delta, @Nullable String message) {
        if (expected != actual && Math.abs(expected - actual) > delta) fail(expected, actual, message);
    }

    public void isEquals(float expected, float actual, float delta) {
        isEquals(expected, actual, delta, null);
    }

    public void isEquals(double expected, double actual, double delta, @Nullable String message) {
        if (expected != actual && Math.abs(expected - actual) > delta) fail(expected, actual, message);
    }

    public void isEquals(double expected, double actual, double delta) {
        isEquals(expected, actual, delta, null);
    }

    public void isNotEquals(@Nullable Object expected, @Nullable Object actual, @Nullable String message) {
        if (Objects.equals(expected, actual)) fail(expected, actual, message);
    }

    public void isNotEquals(@Nullable Object expected, @Nullable Object actual) {
        isNotEquals(expected, actual, null);
    }

    public void isNotEquals(int expected, int actual, @Nullable String message) {
        if (expected == actual) fail(expected, actual, message);
    }

    public void isNotEquals(int expected, int actual) {
        isNotEquals(expected, actual, null);
    }

    public void isNotEquals(boolean expected, boolean actual, @Nullable String message) {
        if (expected == actual) fail(expected, actual, message);
    }

    public void isNotEquals(boolean expected, boolean actual) {
        isNotEquals(expected, actual, null);
    }

    public void isNotEquals(short expected, short actual, @Nullable String message) {
        if (expected == actual) fail(expected, actual, message);
    }

    public void isNotEquals(short expected, short actual) {
        isNotEquals(expected, actual, null);
    }

    public void isNotEquals(byte expected, byte actual, @Nullable String message) {
        if (expected == actual) fail(expected, actual, message);
    }

    public void isNotEquals(byte expected, byte actual) {
        isNotEquals(expected, actual, null);
    }

    public void isNotEquals(char expected, char actual, @Nullable String message) {
        if (expected == actual) fail(expected, actual, message);
    }

    public void isNotEquals(char expected, char actual) {
        isNotEquals(expected, actual, null);
    }

    public void isNotEquals(float expected, float actual, float delta, @Nullable String message) {
        if (expected == actual || Math.abs(expected - actual) <= delta) fail(expected, actual, message);
    }

    public void isNotEquals(float expected, float actual, float delta) {
        isNotEquals(expected, actual, delta, null);
    }

    public void isNotEquals(double expected, double actual, double delta, @Nullable String message) {
        if (expected == actual || Math.abs(expected - actual) <= delta) fail(expected, actual, message);
    }

    public void isNotEquals(double expected, double actual, double delta) {
        isNotEquals(expected, actual, delta, null);
    }
}
