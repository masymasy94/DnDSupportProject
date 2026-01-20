package com.dndplatform.user.domain.event;

public record UserRegisteredEvent(
        Long userId,
        String email,
        String username
) {
}