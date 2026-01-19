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
        if (value != null) {
            conditions.add(field + " = :" + paramName);
            params.put(paramName, value);
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
