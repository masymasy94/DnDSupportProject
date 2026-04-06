package com.dndplatform.documentqa.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record DocumentChunk(
        Long id,
        String documentId,
        int chunkIndex,
        String content
) {
}
