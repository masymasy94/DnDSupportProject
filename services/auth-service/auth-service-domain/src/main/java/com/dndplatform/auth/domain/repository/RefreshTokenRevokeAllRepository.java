package com.dndplatform.auth.domain.repository;

public interface RefreshTokenRevokeAllRepository {
    void revokeAllTokens(long userId);
}
