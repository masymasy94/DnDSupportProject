package com.dndplatform.notificationservice.view.model.vm;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"id", "name", "createdAt"})
@Schema(description = "Email template item")
public record EmailTemplateItemViewModel(

        @Schema(description = "Unique template identifier", example = "123")
        Long id,

        @Schema(description = "Template name", example = "welcome-email")
        String name,

        @Schema(description = "Timestamp when template was created", example = "2026-01-13T10:30:00")
        String createdAt
) {
}
