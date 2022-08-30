package com.github.mjaroslav.mcingametester.loader;

import org.jetbrains.annotations.NotNull;

public class TestException extends Exception {
    public TestException(@NotNull Throwable cause) {
        super(cause);
    }
}
