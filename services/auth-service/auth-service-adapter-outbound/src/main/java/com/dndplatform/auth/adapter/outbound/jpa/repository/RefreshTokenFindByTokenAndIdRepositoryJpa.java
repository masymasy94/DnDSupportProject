package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.adapter.outbound.jpa.entity.RefreshTokenEntity;
import com.dndplatform.auth.adapter.outbound.jpa.mapper.RefreshTokenMapper;
import com.dndplatform.auth.domain.model.RefreshToken;
import com.dndplatform.auth.domain.repository.RefreshTokenFindByTokenAndIdRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class RefreshTokenFindByTokenAndIdRepositoryJpa implements RefreshTokenFindByTokenAndIdRepository, PanacheRepository<RefreshTokenEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final RefreshTokenMapper mapper;

    @Inject
    public RefreshTokenFindByTokenAndIdRepositoryJpa(RefreshTokenMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<RefreshToken> findByTokenAndId(String token, long userId) {

        log.info(() -> "Looking for refresh token for user id %s with token %s".formatted(userId, token));
        return find("token = ?1 and userId = ?2", token, userId)
                .firstResultOptional()
                .map(mapper);
    }
}
