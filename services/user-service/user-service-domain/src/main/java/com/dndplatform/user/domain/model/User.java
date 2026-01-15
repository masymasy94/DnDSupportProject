package com.dndplatform.user.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.time.LocalDateTime;

@Builder
public record User(
        Long id,
        String username,
        String email,
        String passwordHash,
        String role,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
