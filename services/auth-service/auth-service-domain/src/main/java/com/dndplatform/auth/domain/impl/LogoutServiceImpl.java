package com.dndplatform.auth.domain.impl;

import com.dndplatform.auth.domain.LogoutService;
import com.dndplatform.auth.domain.repository.RefreshTokenRevokeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class LogoutServiceImpl implements LogoutService {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final RefreshTokenRevokeRepository refreshTokenRevokeRepository;

    @Inject
    public LogoutServiceImpl(RefreshTokenRevokeRepository refreshTokenRevokeRepository) {
        this.refreshTokenRevokeRepository = refreshTokenRevokeRepository;
    }

    @Override
    public void logout(String token, long userId) {
        log.info(() -> "Logging out user id %s".formatted(userId));
        refreshTokenRevokeRepository.revokeToken(token, userId);
    }
}
