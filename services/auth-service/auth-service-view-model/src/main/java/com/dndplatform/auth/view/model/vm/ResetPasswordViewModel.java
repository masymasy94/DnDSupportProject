package com.dndplatform.auth.view.model.vm;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"token", "newPassword"})
@Schema(description = "Reset password using token")
public record ResetPasswordViewModel(

        @NotBlank(message = "Token is required")
        @Schema(description = "Password reset token from email")
        String token,

        @NotBlank(message = "New password is required")
        @Schema(description = "New password to set")
        String newPassword
) {
}
