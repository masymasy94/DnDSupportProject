package com.dndplatform.auth.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"email"})
@Schema(description = "Request password reset")
@Builder
public record RequestPasswordResetViewModel(

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Schema(description = "Email address associated with the account", example = "gandalf@middleearth.com")
        String email
) {
}
