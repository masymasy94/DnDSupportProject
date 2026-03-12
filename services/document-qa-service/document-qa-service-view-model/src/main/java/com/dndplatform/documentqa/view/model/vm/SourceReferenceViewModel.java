package com.dndplatform.documentqa.view.model.vm;

import com.dndplatform.common.annotations.Builder;

@Builder
public record SourceReferenceViewModel(
        String documentId,
        String fileName,
        String excerpt,
        double score
) {
}
