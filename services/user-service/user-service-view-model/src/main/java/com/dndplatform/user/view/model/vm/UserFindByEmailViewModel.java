package com.dndplatform.user.view.model.vm;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"email"})
@Schema(description = "Find user by email request")
public record UserFindByEmailViewModel(

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Schema(description = "Email address to look up", example = "gandalf@middleearth.com")
        String email
) {
}
