package com.dndplatform.auth.view.model.vm;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"email"})
@Schema(description = "Request OTP login code")
public record RequestOtpLoginViewModel(

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Schema(description = "Email address associated with the account", example = "gandalf@middleearth.com")
        String email
) {
}
