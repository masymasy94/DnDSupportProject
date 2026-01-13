package com.dndplatform.auth.domain.impl;

import com.dndplatform.auth.domain.LogoutAllService;
import com.dndplatform.auth.domain.repository.RefreshTokenRevokeAllRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class LogoutAllServiceImpl implements LogoutAllService {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final RefreshTokenRevokeAllRepository refreshTokenRevokeAllRepository;

    @Inject
    public LogoutAllServiceImpl(RefreshTokenRevokeAllRepository refreshTokenRevokeAllRepository) {
        this.refreshTokenRevokeAllRepository = refreshTokenRevokeAllRepository;
    }

    @Override
    public void logoutAll(long userId) {
        log.info(() -> "Logging out all sessions for user id %s".formatted(userId));
        refreshTokenRevokeAllRepository.revokeAllTokens(userId);
    }
}
