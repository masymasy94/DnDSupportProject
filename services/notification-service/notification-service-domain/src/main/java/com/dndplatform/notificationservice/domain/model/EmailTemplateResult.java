package com.dndplatform.notificationservice.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.time.LocalDateTime;

@Builder
public record EmailTemplateResult(
        Long id,
        String name,
        LocalDateTime createdAt
) {
}
