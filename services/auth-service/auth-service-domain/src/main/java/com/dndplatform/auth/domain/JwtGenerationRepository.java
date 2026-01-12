package com.dndplatform.auth.domain;

import com.dndplatform.auth.domain.model.CreateLoginTokenResponse;
import com.dndplatform.auth.domain.model.RefreshToken;
import com.dndplatform.auth.domain.model.User;

public interface JwtGenerationRepository {
    CreateLoginTokenResponse generateTokenPair(User user, RefreshToken refreshToken);
}
