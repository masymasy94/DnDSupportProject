package com.dndplatform.test.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(DeleteEntitiesList.class)
public @interface DeleteEntities {
    Class<?> from();
    int expectedRowCount() default -1;
}
