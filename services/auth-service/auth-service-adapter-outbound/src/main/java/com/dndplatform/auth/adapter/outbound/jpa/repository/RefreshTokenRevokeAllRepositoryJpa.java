package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.domain.repository.RefreshTokenRevokeAllRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class RefreshTokenRevokeAllRepositoryJpa implements RefreshTokenRevokeAllRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final RefreshTokenPanacheRepository panacheRepository;

    @Inject
    public RefreshTokenRevokeAllRepositoryJpa(RefreshTokenPanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    @Transactional
    public void revokeAllTokens(long userId) {
        log.info(() -> "Revoking all refresh tokens for user id %s".formatted(userId));
        panacheRepository.update("revoked = true where userId = ?1", userId);
    }
}
