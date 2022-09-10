package com.github.mjaroslav.mcingametester.api;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation on non-static void method without parameters for marking it as test.
 * Test methods should be in classes with {@link Common}, {@link Server} or {@link Client} annotation.
 * Test must throw {@link AssertionError} if this should be failed. Test can also throw expected throwable
 * and fails if it throwable not thrown. You can use {@link Assert} for help in throwing AssertionErrors.
 *
 * @see Common
 * @see Server
 * @see Client
 * @see BeforeEach
 * @see AfterEach
 * @see Assert
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
    /**
     * Expected throwable that must be thrown while test for its success resulting.
     *
     * @return Class of expected throwable, no one exception expected if this parameter not present.
     */
    @NotNull Class<? extends Throwable> expected() default None.class;

    final class None extends Throwable {
    }
}
