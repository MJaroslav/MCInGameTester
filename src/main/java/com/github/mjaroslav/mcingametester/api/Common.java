package com.github.mjaroslav.mcingametester.api;

import cpw.mods.fml.common.LoaderState;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation (or {@link Client} or {@link Server}) on class for mark it as test container.
 * In test container you can use all other annotations of this package.
 *
 * @see Client
 * @see Server
 * @see BeforeClass
 * @see AfterClass
 * @see Test
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Common {
    /**
     * LoadState when tests of this test container should be executed.
     *
     * @return LoaderState, AVAILABLE if parameter not present.
     */
    @NotNull LoaderState when() default LoaderState.AVAILABLE;
}
