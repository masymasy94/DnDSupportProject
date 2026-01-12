package com.dndplatform.auth.view.model.vm;

import com.dndplatform.common.annotations.Builder;

@Builder
public record RefreshLoginTokenResponseViewModel(
        String accessToken,
        long accessTokenExpiresAt
) {
}
