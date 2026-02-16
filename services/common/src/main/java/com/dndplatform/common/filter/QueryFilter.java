package com.dndplatform.common.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A fluent builder for constructing dynamic queries with optional filters.
 * Only non-null values are added to the query.
 */
public class QueryFilter {

    private final List<String> conditions = new ArrayList<>();
    private final Map<String, Object> params = new HashMap<>();

    public QueryFilter add(String field, Object value) {
        return add(field, field, value);
    }

    public QueryFilter add(String field, String paramName, Object value) {
        return add(field, paramName, value, Param.Operator.EQUALS);
    }

    public QueryFilter add(String field, String paramName, Object value, Param.Operator operator) {
        if (value != null) {
            switch (operator) {
                case LIKE -> {
                    conditions.add("lower(" + field + ") LIKE :" + paramName);
                    params.put(paramName, "%" + value.toString().toLowerCase() + "%");
                }
                case EQUALS -> {
                    conditions.add(field + " = :" + paramName);
                    params.put(paramName, value);
                }
                case IN -> {
                    if (value instanceof List<?> list && !list.isEmpty()) {
                        conditions.add(field + " IN (:" + paramName + ")");
                        params.put(paramName, list);
                    }
                }
                case LIKE_ANY -> {
                    String[] fields = field.split(",");
                    String searchValue = "%" + value.toString().toLowerCase() + "%";
                    StringBuilder orCondition = new StringBuilder("(");
                    for (int i = 0; i < fields.length; i++) {
                        if (i > 0) orCondition.append(" or ");
                        orCondition.append("lower(").append(fields[i].trim()).append(") LIKE :").append(paramName);
                    }
                    orCondition.append(")");
                    conditions.add(orCondition.toString());
                    params.put(paramName, searchValue);
                }
            }
        }
        return this;
    }

    public String query() {
        return String.join(" and ", conditions);
    }

    public Map<String, Object> params() {
        return params;
    }

    public boolean isEmpty() {
        return conditions.isEmpty();
    }

    public QueryFilter merge(QueryFilter other) {
        conditions.addAll(other.conditions);
        params.putAll(other.params);
        return this;
    }
}
