package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.adapter.outbound.jpa.entity.RefreshTokenEntity;
import com.dndplatform.auth.domain.repository.RefreshTokenRevokeAllRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class RefreshTokenRevokeAllRepositoryJpa implements RefreshTokenRevokeAllRepository, PanacheRepository<RefreshTokenEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());

    @Override
    @Transactional
    public void revokeAllTokens(long userId) {
        log.info(() -> "Revoking all refresh tokens for user id %s".formatted(userId));
        update("revoked = true where userId = ?1", userId);
    }
}
