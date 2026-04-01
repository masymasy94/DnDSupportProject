package com.dndplatform.documentqa.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record SourceReference(
        String documentId,
        String fileName,
        String excerpt,
        double score
) {
}
