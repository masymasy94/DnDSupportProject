package com.dndplatform.auth.domain.service;

import com.dndplatform.auth.domain.model.CreateLoginTokenResponse;
import com.dndplatform.auth.domain.model.RefreshToken;
import com.dndplatform.auth.domain.model.User;

public interface JwtTokenService {
    CreateLoginTokenResponse generateTokenPair(User user, RefreshToken refreshToken);
}
