package com.dndplatform.common.filter;

/**
 * Represents a query parameter with field, param name, value, and operator.
 * Use the static factory methods to create instances.
 */
public record Param(String field, String paramName, Object value, Operator operator) {

    public enum Operator {
        EQUALS,
        LIKE
    }

    public static Param of(String field, Object value) {
        return new Param(field, field, value, Operator.EQUALS);
    }

    public static Param of(String field, String paramName, Object value) {
        return new Param(field, paramName, value, Operator.EQUALS);
    }

    public static Param like(String field, Object value) {
        return new Param(field, field, value, Operator.LIKE);
    }

    public static Param like(String field, String paramName, Object value) {
        return new Param(field, paramName, value, Operator.LIKE);
    }
}
