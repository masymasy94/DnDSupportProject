package com.dndplatform.documentqa.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.time.LocalDateTime;

@Builder
public record Conversation(
        Long id,
        Long userId,
        Long campaignId,
        String title,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
