package com.github.mjaroslav.mcingametester.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
@AllArgsConstructor
public class ComparisonError extends AssertionError {
    private final @Nullable Object expected, actual;

    public ComparisonError(@Nullable String message, @Nullable Object expected, @Nullable Object actual) {
        super(message);
        this.expected = expected;
        this.actual = actual;
    }
}
