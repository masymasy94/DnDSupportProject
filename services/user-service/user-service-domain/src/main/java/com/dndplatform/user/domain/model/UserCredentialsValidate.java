package com.dndplatform.user.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record UserCredentialsValidate(
        String username,
        String password
) {
}
