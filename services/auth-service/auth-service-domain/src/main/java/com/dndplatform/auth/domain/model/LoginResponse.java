package com.dndplatform.auth.domain.model;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        long accessTokenExpiresAt,
        long refreshTokenExpiresAt
) {
}
