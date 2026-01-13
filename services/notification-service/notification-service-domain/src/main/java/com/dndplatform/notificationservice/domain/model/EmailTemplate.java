package com.dndplatform.notificationservice.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record EmailTemplate(
        String name,
        String subject,
        String htmlContent,
        String description
) {
}
