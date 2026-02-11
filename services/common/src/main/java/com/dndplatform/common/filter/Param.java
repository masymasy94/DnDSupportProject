package com.dndplatform.common.filter;

import java.util.List;

/**
 * Represents a query parameter with field, param name, value, and operator.
 * Use the static factory methods to create instances.
 */
public record Param(String field, String paramName, Object value, Operator operator) {

    public enum Operator {
        EQUALS,
        LIKE,
        IN,
        LIKE_ANY
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

    public static Param in(String field, List<?> values) {
        return new Param(field, field, values, Operator.IN);
    }

    public static Param in(String field, String paramName, List<?> values) {
        return new Param(field, paramName, values, Operator.IN);
    }

    /**
     * Creates a LIKE condition that matches ANY of the specified fields.
     * @param fields comma-separated list of fields to search in
     * @param paramName the parameter name
     * @param value the search value
     */
    public static Param likeAny(String fields, String paramName, Object value) {
        return new Param(fields, paramName, value, Operator.LIKE_ANY);
    }
}
