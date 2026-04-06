package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.adapter.outbound.jpa.mapper.RefreshTokenMapper;
import com.dndplatform.auth.domain.model.RefreshToken;
import com.dndplatform.auth.domain.repository.RefreshTokenFindByTokenAndIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class RefreshTokenFindByTokenAndIdRepositoryJpa implements RefreshTokenFindByTokenAndIdRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final RefreshTokenMapper mapper;
    private final RefreshTokenPanacheRepository panacheRepository;

    @Inject
    public RefreshTokenFindByTokenAndIdRepositoryJpa(RefreshTokenMapper mapper,
                                                     RefreshTokenPanacheRepository panacheRepository) {
        this.mapper = mapper;
        this.panacheRepository = panacheRepository;
    }

    @Override
    public Optional<RefreshToken> findByTokenAndId(String token, long userId) {

        log.info(() -> "Looking for refresh token for user id %s with token %s".formatted(userId, token));
        return panacheRepository.find("token = ?1 and userId = ?2", token, userId)
                .firstResultOptional()
                .map(mapper);
    }
}
