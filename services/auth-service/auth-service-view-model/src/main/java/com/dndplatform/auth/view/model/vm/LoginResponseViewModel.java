package com.dndplatform.auth.view.model.vm;

import com.dndplatform.common.annotations.Builder;

@Builder
public record LoginResponseViewModel(
        String accessToken,
        String refreshToken,
        long accessTokenExpiresAt,
        long refreshTokenExpiresAt,
        String tokenType
) {
}
