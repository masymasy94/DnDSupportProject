package com.dndplatform.test.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NamedParam {
    /** Name of the JPQL parameter. Default = field name. */
    String value() default "";
}
