package com.dndplatform.auth.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.time.LocalDateTime;

@Builder
public record PasswordResetToken(Long id,
                                  String token,
                                  Long userId,
                                  LocalDateTime expiresAt,
                                  Boolean used,
                                  LocalDateTime createdAt) {
}
