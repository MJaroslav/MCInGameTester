package com.github.mjaroslav.mcingametester.api;

import cpw.mods.fml.common.LoaderState;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Same as {@link Common} but for only client side and can use {@link WorldShadow}
 *
 * @see Common
 * @see WorldShadow
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Server {
    /**
     * LoadState when tests of this test container should be executed.
     *
     * @return LoaderState, AVAILABLE if parameter not present.
     */
    @NotNull LoaderState when() default LoaderState.AVAILABLE;
}
