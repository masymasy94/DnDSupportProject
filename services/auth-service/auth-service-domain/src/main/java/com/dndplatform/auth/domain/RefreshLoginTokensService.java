package com.dndplatform.auth.domain;

import com.dndplatform.auth.domain.model.CreateLoginTokenResponse;

public interface RefreshLoginTokensService {
    CreateLoginTokenResponse refreshLoginTokens(String refreshToken, long userId);
}
