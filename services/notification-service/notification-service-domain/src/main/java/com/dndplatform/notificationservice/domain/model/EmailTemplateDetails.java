package com.dndplatform.notificationservice.domain.model;

public record EmailTemplateDetails(
        Long id,
        String name,
        String subject,
        String htmlContent
) {
}
