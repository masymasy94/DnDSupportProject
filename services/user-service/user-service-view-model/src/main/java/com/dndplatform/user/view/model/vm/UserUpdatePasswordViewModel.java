package com.dndplatform.user.view.model.vm;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"newPassword"})
@Schema(description = "Update password request")
public record UserUpdatePasswordViewModel(

        @NotBlank(message = "New password is required")
        @Schema(description = "New password")
        String newPassword
) {
}
