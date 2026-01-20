package com.dndplatform.notificationservice.view.model.vm;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"messageId", "status", "sentAt", "errorMessage"})
@Schema(description = "Email send response")
public record SendEmailResponseViewModel(

        @Schema(description = "Unique message identifier", example = "msg-123456")
        String messageId,

        @Schema(description = "Email status", example = "SENT")
        String status,

        @Schema(description = "Timestamp when email was sent", example = "2026-01-12T10:30:00Z")
        String sentAt,

        @Schema(description = "Error message if failed")
        String errorMessage
) {
}
