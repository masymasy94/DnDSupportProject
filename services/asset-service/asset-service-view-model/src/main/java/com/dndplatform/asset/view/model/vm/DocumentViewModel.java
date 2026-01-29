package com.dndplatform.asset.view.model.vm;

import java.time.Instant;

public record DocumentViewModel(
        String id,
        String fileName,
        String contentType,
        long size,
        String uploadedBy,
        Instant uploadedAt
) {
}
