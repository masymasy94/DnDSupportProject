package com.dndplatform.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generates a builder pattern implementation for the annotated class or record.
 * <p>
 * Generated builder includes:
 * <ul>
 *   <li>Static inner Builder class</li>
 *   <li>Fluent withField() methods for each field/component</li>
 *   <li>build() method to create instances</li>
 *   <li>toBuilder() method on the original type for copying</li>
 * </ul>
 * <p>
 * Supports both Java records and regular classes.
 * <p>
 * Example usage with record:
 * <pre>
 * {@literal @}Builder
 * public record User(String name, int age) {}
 *
 * // Usage:
 * User user = User.builder()
 *     .withName("John")
 *     .withAge(30)
 *     .build();
 *
 * User copy = user.toBuilder()
 *     .withAge(31)
 *     .build();
 * </pre>
 * <p>
 * Example usage with class:
 * <pre>
 * {@literal @}Builder
 * public class User {
 *     private String name;
 *     private int age;
 *     // getters and setters required
 * }
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface Builder {

    /**
     * Prefix for setter methods in the builder.
     * Default is "with" (e.g., withName, withAge).
     */
    String setterPrefix() default "with";

    /**
     * Whether to generate toBuilder() method.
     * Default is true.
     */
    boolean toBuilder() default true;

    /**
     * Whether to make the builder class public.
     * Default is true.
     */
    boolean publicBuilder() default true;
}