package com.dndplatform.auth.domain.repository;

import com.dndplatform.auth.domain.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenFindByTokenAndIdRepository {
    Optional<RefreshToken> findByTokenAndId(String token, long userId);
}
