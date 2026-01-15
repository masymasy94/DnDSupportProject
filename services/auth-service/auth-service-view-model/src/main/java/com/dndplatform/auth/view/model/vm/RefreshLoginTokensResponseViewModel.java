package com.dndplatform.auth.view.model.vm;

import com.dndplatform.common.annotations.Builder;

@Builder
public record RefreshLoginTokensResponseViewModel(
        String accessToken,
        long accessTokenExpiresAt,
        String tokenType
) {
}
