package com.dndplatform.documentqa.view.model.vm;

import com.dndplatform.common.annotations.Builder;

import java.time.LocalDateTime;

@Builder
public record ConversationViewModel(
        Long id,
        Long userId,
        Long campaignId,
        String title,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
