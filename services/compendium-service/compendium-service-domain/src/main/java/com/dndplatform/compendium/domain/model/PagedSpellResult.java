package com.dndplatform.compendium.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.util.List;

@Builder
public record PagedSpellResult(
        List<Spell> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {}
