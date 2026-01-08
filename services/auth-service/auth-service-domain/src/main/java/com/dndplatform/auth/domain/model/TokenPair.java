package com.dndplatform.auth.domain.model;

public record TokenPair(
        String accessToken,
        String refreshToken,
        long accessTokenExpiresAt,
        long refreshTokenExpiresAt
) {
}
