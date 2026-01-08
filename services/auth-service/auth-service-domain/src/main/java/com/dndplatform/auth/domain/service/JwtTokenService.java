package com.dndplatform.auth.domain.service;

import com.dndplatform.auth.domain.model.RefreshToken;
import com.dndplatform.auth.domain.model.TokenPair;
import com.dndplatform.auth.domain.model.User;

public interface JwtTokenService {
    TokenPair generateTokenPair(User user, RefreshToken refreshToken);
}
