package com.dndplatform.user.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@JsonbPropertyOrder({"id", "username", "email", "role", "active", "createdAt"})
@Schema(description = "User response")
@Builder
public record UserViewModel(

        @Schema(description = "User ID", example = "42")
        Long id,

        @Schema(description = "Username", example = "gandalf_grey")
        String username,

        @Schema(description = "Email address", example = "gandalf@middleearth.com")
        String email,

        @Schema(description = "User role", example = "PLAYER")
        String role,

        @Schema(description = "Whether the user account is active", example = "true")
        Boolean active,

        @Schema(description = "Account creation timestamp")
        LocalDateTime createdAt
) {
}
