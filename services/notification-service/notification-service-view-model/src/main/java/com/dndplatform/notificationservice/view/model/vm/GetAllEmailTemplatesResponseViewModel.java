package com.dndplatform.notificationservice.view.model.vm;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@JsonbPropertyOrder({"templates"})
@Schema(description = "Response containing all email templates")
public record GetAllEmailTemplatesResponseViewModel(

        @Schema(description = "List of email templates")
        List<EmailTemplateItemViewModel> templates
) {
}
