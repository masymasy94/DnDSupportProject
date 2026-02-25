package com.dndplatform.notificationservice.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record Email(
        String to,
        List<String> cc,
        List<String> bcc,
        String subject,
        String textBody,
        String htmlBody,
        Long templateId,
        Map<String, String> templateVariables,
        List<EmailAttachment> attachments
) {
}
