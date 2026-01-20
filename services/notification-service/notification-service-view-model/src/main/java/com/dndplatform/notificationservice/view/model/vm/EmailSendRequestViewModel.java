package com.dndplatform.notificationservice.view.model.vm;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import com.dndplatform.common.annotations.Builder;

import java.util.List;


@Builder
@JsonbPropertyOrder({"to", "cc", "bcc", "templateId", "textBody", "attachments"})
@Schema(description = "Email send request")
public record EmailSendRequestViewModel(

        @NotBlank(message = "Recipient email is required")
        @Email(message = "Invalid recipient email format")
        @Schema(description = "Primary recipient email", example = "user@example.com")
        String to,

        @Schema(description = "CC recipients")
        List<String> cc,

        @Schema(description = "BCC recipients")
        List<String> bcc,

        @NotNull(message = "Template ID is required")
        @Schema(description = "Template ID for email subject and body", example = "1")
        Long templateId,

        @Schema(description = "Plain text body (optional override)")
        String textBody,

        @Schema(description = "File attachments")
        List<EmailAttachmentViewModel> attachments
) {
}
