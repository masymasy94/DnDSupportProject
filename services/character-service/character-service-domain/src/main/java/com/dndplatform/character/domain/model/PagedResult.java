package com.dndplatform.character.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.util.List;

@Builder
public record PagedResult(
        List<CharacterSummary> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
