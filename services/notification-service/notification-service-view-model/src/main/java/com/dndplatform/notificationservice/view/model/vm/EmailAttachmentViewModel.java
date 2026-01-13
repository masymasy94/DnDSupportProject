package com.dndplatform.notificationservice.view.model.vm;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"fileName", "contentType", "data"})
@Schema(description = "Email attachment")
public record EmailAttachmentViewModel(

        @NotBlank(message = "File name is required")
        @Schema(description = "Attachment file name", example = "document.pdf")
        String fileName,

        @NotBlank(message = "Content type is required")
        @Schema(description = "MIME content type", example = "application/pdf")
        String contentType,

        @NotBlank(message = "Data is required")
        @Schema(description = "Base64 encoded file data")
        String data
) {
}
