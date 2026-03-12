package com.dndplatform.asset.domain.model;

import java.time.Instant;

public record Document(
        String id,
        String fileName,
        String contentType,
        long size,
        String uploadedBy,
        Instant uploadedAt,
        RagStatus ragStatus
) {
    public Document(String id, String fileName, String contentType, long size, String uploadedBy, Instant uploadedAt) {
        this(id, fileName, contentType, size, uploadedBy, uploadedAt, RagStatus.PENDING);
    }
}
