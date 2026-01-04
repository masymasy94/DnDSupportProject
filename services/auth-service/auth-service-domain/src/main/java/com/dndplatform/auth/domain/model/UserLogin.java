package com.dndplatform.auth.domain.model;

import com.dndplatform.common.annotations.Builder;


@Builder
public record UserLogin(String username,
                        String password) {
}

