package com.dndplatform.compendium.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.util.List;

@Builder
public record PagedSpeciesResult(
        List<Species> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {}
