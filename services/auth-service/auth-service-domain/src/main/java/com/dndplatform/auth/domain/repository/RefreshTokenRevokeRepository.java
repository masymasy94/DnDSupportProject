package com.dndplatform.auth.domain.repository;

public interface RefreshTokenRevokeRepository {
    void revokeToken(String token, long userId);
}
