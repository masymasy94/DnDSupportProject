package com.dndplatform.common.filter;

/**
 * Represents a query parameter with field, param name, and value.
 * Use the static factory methods to create instances.
 */
public record Param(String field, String paramName, Object value) {

    public static Param of(String field, Object value) {
        return new Param(field, field, value);
    }

    public static Param of(String field, String paramName, Object value) {
        return new Param(field, paramName, value);
    }
}
