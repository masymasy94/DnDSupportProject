package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.adapter.outbound.jpa.entity.RefreshTokenEntity;
import com.dndplatform.auth.domain.repository.RefreshTokenRevokeRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class RefreshTokenRevokeRepositoryJpa implements RefreshTokenRevokeRepository, PanacheRepository<RefreshTokenEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());

    @Override
    @Transactional
    public void revokeToken(String token, long userId) {
        log.info(() -> "Revoking refresh token for user id %s".formatted(userId));
        update("revoked = true where token = ?1 and userId = ?2", token, userId);
    }
}
