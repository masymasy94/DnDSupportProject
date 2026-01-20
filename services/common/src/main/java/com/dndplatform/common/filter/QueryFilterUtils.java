package com.dndplatform.common.filter;

import java.util.Arrays;
import java.util.stream.Collector;

public final class QueryFilterUtils {

    private QueryFilterUtils() {}

    private static final Collector<Param, QueryFilter, QueryFilter> QUERY_FILTER_COLLECTOR =
            Collector.of(
                    QueryFilter::new,
                    (filter, param) -> filter.add(param.field(), param.paramName(), param.value(), param.operator()),
                    QueryFilter::merge
            );

    public static QueryFilter create(Param... params) {
        return Arrays.stream(params).collect(QUERY_FILTER_COLLECTOR);
    }

    public static QueryFilter create(FilterCriteria criteria) {
        return criteria.toParams().stream().collect(QUERY_FILTER_COLLECTOR);
    }
}
