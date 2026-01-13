package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.adapter.outbound.jpa.entity.RefreshTokenEntity;
import com.dndplatform.auth.adapter.outbound.jpa.entity.RefreshTokenEntityBuilder;
import com.dndplatform.auth.adapter.outbound.jpa.mapper.RefreshTokenMapper;
import com.dndplatform.auth.domain.model.RefreshToken;
import com.dndplatform.auth.domain.repository.RefreshTokenCreateRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Logger;

@Transactional
@ApplicationScoped
public class RefreshTokenCreateRepositoryJpa implements RefreshTokenCreateRepository, PanacheRepository<RefreshTokenEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final RefreshTokenMapper mapper;
    @ConfigProperty(name = "jwt.refresh-token-expiry-days", defaultValue = "30")
    long refreshTokenExpiryDays;

    @Inject
    public RefreshTokenCreateRepositoryJpa(RefreshTokenMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public RefreshToken createRefreshToken(long userId) {

        log.info(() -> "Creating refresh token for user %s".formatted(userId));

        LocalDateTime now =  LocalDateTime.now();
        var entity = buildRefreshTokenEntity(userId, now, now.plusDays(refreshTokenExpiryDays));
        persist(entity);

        return mapper.apply(entity);
    }

    private static RefreshTokenEntity buildRefreshTokenEntity(long userId, LocalDateTime now, LocalDateTime expiresAt) {
        return RefreshTokenEntityBuilder.builder()
                .withCreatedAt(now)
                .withExpiresAt(expiresAt)
                .withUserId(userId)
                .withToken(UUID.randomUUID().toString())
                .withRevoked(false)
                .build();
    }
}
