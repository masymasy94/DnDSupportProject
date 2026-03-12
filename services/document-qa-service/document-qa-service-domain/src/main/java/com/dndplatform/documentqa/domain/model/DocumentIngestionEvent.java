package com.dndplatform.documentqa.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record DocumentIngestionEvent(
        String documentId,
        String fileName,
        String contentType,
        String uploadedBy
) {
}
