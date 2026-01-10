package com.dndplatform.user.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"username", "password"})
@Schema(description = "Credentials verification request")
@Builder
public record UserCredentialsValidateViewModel(

        @NotBlank(message = "Username is required")
        @Schema(description = "Username", examples = "gandalf_grey")
        String username,

        @NotBlank(message = "Password is required")
        @Schema(description = "Password", examples = "YouShallNotPass1!")
        String password
) {
}
