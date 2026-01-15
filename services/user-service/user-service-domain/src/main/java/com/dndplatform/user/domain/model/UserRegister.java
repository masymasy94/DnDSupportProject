package com.dndplatform.user.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record UserRegister(
        String username,
        String email,
        String password
) {
}
