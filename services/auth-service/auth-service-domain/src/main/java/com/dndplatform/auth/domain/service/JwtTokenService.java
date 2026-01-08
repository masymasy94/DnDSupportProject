package com.dndplatform.auth.domain.service;

import com.dndplatform.auth.domain.model.RefreshToken;
import com.dndplatform.auth.domain.model.LoginResponse;
import com.dndplatform.auth.domain.model.User;

public interface JwtTokenService {
    LoginResponse generateTokenPair(User user, RefreshToken refreshToken);
}
