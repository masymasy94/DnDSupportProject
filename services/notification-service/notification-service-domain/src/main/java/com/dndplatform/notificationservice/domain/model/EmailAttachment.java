package com.dndplatform.notificationservice.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record EmailAttachment(
        String fileName,
        String contentType,
        byte[] data
) {
}
