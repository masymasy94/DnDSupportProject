package com.dndplatform.notificationservice.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.time.Instant;

@Builder
public record EmailResult(
        String messageId,
        EmailStatus status,
        Instant sentAt,
        String errorMessage
) {
}
