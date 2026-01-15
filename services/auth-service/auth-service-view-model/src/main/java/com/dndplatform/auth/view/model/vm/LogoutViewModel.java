package com.dndplatform.auth.view.model.vm;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record LogoutViewModel(@Schema(description = "Refresh token to be revoked", required = true) String refreshToken,
                              @Schema(description = "User ID", required = true, examples = "123") long userId) {
}
