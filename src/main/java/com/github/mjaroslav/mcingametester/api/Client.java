package com.github.mjaroslav.mcingametester.api;

import cpw.mods.fml.common.LoaderState;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Client {
    @NotNull LoaderState value() default LoaderState.AVAILABLE;
}
