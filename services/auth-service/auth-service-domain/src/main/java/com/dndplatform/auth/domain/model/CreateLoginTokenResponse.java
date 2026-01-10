package com.dndplatform.auth.domain.model;

public record CreateLoginTokenResponse(
        String accessToken,
        String refreshToken,
        long accessTokenExpiresAt,
        long refreshTokenExpiresAt
) {
}
