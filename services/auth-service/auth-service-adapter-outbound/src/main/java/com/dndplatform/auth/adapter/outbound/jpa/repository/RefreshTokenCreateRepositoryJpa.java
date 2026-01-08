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

import java.time.LocalDateTime;
import java.util.UUID;

@Transactional
@ApplicationScoped
public class RefreshTokenCreateRepositoryJpa implements RefreshTokenCreateRepository, PanacheRepository<RefreshTokenEntity> {

    private final RefreshTokenMapper mapper;

    @Inject
    public RefreshTokenCreateRepositoryJpa(RefreshTokenMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public RefreshToken createRefreshToken(long userId) {
        var entity = RefreshTokenEntityBuilder.builder()
                .withCreatedAt(LocalDateTime.now())
                .withUserId(userId)
                .withToken(UUID.randomUUID().toString())
                .withRevoked(false)
                .build();

        persist(entity);
        return mapper.apply(entity);
    }
}
