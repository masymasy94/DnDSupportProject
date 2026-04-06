package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.domain.repository.RefreshTokenRevokeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class RefreshTokenRevokeRepositoryJpa implements RefreshTokenRevokeRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final RefreshTokenPanacheRepository panacheRepository;

    @Inject
    public RefreshTokenRevokeRepositoryJpa(RefreshTokenPanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    @Transactional
    public void revokeToken(String token, long userId) {
        log.info(() -> "Revoking refresh token for user id %s".formatted(userId));
        panacheRepository.update("revoked = true where token = ?1 and userId = ?2", token, userId);
    }
}
