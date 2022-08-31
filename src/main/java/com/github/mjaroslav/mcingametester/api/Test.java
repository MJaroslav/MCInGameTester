package com.github.mjaroslav.mcingametester.api;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
    @NotNull Class<? extends Throwable> expected() default None.class;

    final class None extends Throwable {
    }
}
