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
    /**
     * JPQL where clause body (without the "WHERE" keyword), referencing the
     * entity alias `e` and named parameters bound to {@link NamedParam} fields
     * on the test class. Example: {@code where = "e.username = :username"}.
     */
    String where() default "";
}
