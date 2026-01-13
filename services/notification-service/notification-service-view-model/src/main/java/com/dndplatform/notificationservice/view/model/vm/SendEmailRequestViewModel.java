package com.dndplatform.notificationservice.view.model.vm;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@JsonbPropertyOrder({"to", "cc", "bcc", "subject", "textBody", "htmlBody", "templateName", "templateData", "attachments", "priority", "replyTo"})
@Schema(description = "Email send request")
public record SendEmailRequestViewModel(

        @NotBlank(message = "Recipient email is required")
        @Email(message = "Invalid recipient email format")
        @Schema(description = "Primary recipient email", example = "user@example.com")
        String to,

        @Schema(description = "CC recipients")
        List<String> cc,

        @Schema(description = "BCC recipients")
        List<String> bcc,

        @NotBlank(message = "Subject is required")
        @Schema(description = "Email subject", example = "Welcome to DnD Platform")
        String subject,

        @Schema(description = "Plain text body")
        String textBody,

        @Schema(description = "HTML body (mutually exclusive with templateName)")
        String htmlBody,

        @Schema(description = "Template name for rendering HTML body", example = "generic")
        String templateName,

        @Schema(description = "Template variables for rendering")
        Map<String, Object> templateData,

        @Schema(description = "File attachments")
        List<EmailAttachmentViewModel> attachments
) {
}
