package com.dndplatform.auth.domain.model;

import java.time.LocalDateTime;

public record RefreshToken(
         String token,
         long userId,
         LocalDateTime expiresAt,
         Boolean revoked,
         LocalDateTime createdAt) {
}
