package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.adapter.outbound.jpa.entity.RefreshTokenEntity;
import com.dndplatform.auth.adapter.outbound.jpa.entity.RefreshTokenEntityBuilder;
import com.dndplatform.auth.adapter.outbound.jpa.mapper.RefreshTokenMapper;
import com.dndplatform.auth.domain.repository.RefreshTokenCreateRepository;
import com.dndplatform.auth.domain.model.RefreshToken;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Transactional
@ApplicationScoped
public class RefreshTokenCreateRepositoryJpa implements RefreshTokenCreateRepository, PanacheRepository<RefreshTokenEntity> {

    private final RefreshTokenMapper mapper;
    private final UserFindByIdRepositoryJpa userFindByIdRepository;

    @Inject
    public RefreshTokenCreateRepositoryJpa(RefreshTokenMapper mapper, UserFindByIdRepositoryJpa userFindByIdRepository) {
        this.mapper = mapper;
        this.userFindByIdRepository = userFindByIdRepository;
    }

    @Override
    public RefreshToken createRefreshToken(long userId) {

        var userEntity = Optional.ofNullable(userFindByIdRepository.findById(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        var entity = RefreshTokenEntityBuilder.builder()
                .withCreatedAt(LocalDateTime.now())
                .withUser(userEntity)
                .withToken(UUID.randomUUID().toString())
                .build();

        persist(entity);
        return mapper.apply(entity);

    }
}
