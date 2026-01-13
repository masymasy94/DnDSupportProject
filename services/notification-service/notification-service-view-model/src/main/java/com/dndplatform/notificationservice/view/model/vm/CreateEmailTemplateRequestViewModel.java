package com.dndplatform.notificationservice.view.model.vm;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"name", "subject", "htmlContent", "description"})
@Schema(description = "Email template creation request")
public record CreateEmailTemplateRequestViewModel(

        @NotBlank(message = "Template name is required")
        @Size(max = 100, message = "Template name must not exceed 100 characters")
        @Schema(description = "Unique template name", example = "welcome-email")
        String name,

        @Size(max = 255, message = "Subject must not exceed 255 characters")
        @Schema(description = "Default email subject for this template", example = "Welcome to DnD Platform!")
        String subject,

        @NotBlank(message = "HTML content is required")
        @Schema(description = "Qute HTML template content", example = "<html><body>Hello {name}!</body></html>")
        String htmlContent,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        @Schema(description = "Template description for documentation", example = "Welcome email sent to new users")
        String description
) {
}
