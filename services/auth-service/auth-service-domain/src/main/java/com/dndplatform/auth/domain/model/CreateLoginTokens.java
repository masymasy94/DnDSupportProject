package com.dndplatform.auth.domain.model;

import com.dndplatform.common.annotations.Builder;


@Builder
public record CreateLoginTokens(String username,
                                String password) {
}

