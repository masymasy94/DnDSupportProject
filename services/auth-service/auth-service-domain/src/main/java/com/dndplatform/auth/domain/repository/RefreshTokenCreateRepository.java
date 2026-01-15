package com.dndplatform.auth.domain.repository;

import com.dndplatform.auth.domain.model.RefreshToken;


public interface RefreshTokenCreateRepository {
    RefreshToken createRefreshToken(long userId);
}
