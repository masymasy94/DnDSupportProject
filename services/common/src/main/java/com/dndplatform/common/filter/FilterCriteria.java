package com.dndplatform.common.filter;

import java.util.List;

/**
 * Interface for domain-specific filter criteria.
 * Implement this in each domain to define filterable fields.
 */
public interface FilterCriteria {

    /**
     * Returns the list of filter parameters.
     * Only non-null values will be included in the query.
     */
    List<Param> toParams();
}
