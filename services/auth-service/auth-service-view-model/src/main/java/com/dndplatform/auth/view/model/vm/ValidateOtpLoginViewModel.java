package com.dndplatform.auth.view.model.vm;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"email", "otpCode"})
@Schema(description = "Validate OTP login code")
public record ValidateOtpLoginViewModel(

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Schema(description = "Email address associated with the account", example = "gandalf@middleearth.com")
        String email,

        @NotBlank(message = "OTP code is required")
        @Schema(description = "6-digit OTP code from email", example = "123456")
        String otpCode
) {
}
