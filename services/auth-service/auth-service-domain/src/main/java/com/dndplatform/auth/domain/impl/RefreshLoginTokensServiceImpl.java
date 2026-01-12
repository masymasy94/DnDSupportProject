package com.dndplatform.auth.domain.impl;

import com.dndplatform.auth.domain.JwtGenerationRepository;
import com.dndplatform.auth.domain.RefreshLoginTokensService;
import com.dndplatform.auth.domain.model.CreateLoginTokenResponse;
import com.dndplatform.auth.domain.repository.RefreshTokenFindByTokenAndIdRepository;
import com.dndplatform.auth.domain.repository.UserFindByIdRepository;
import com.dndplatform.common.exception.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class RefreshLoginTokensServiceImpl implements RefreshLoginTokensService {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final RefreshTokenFindByTokenAndIdRepository refreshTokenFindRepository;
    private final UserFindByIdRepository userFindByIdRepository;
    private final JwtGenerationRepository jwtGenerationRepository;

    @Inject
    public RefreshLoginTokensServiceImpl(RefreshTokenFindByTokenAndIdRepository refreshTokenFindRepository,
                                         UserFindByIdRepository userFindByIdRepository,
                                         JwtGenerationRepository jwtGenerationRepository) {
        this.refreshTokenFindRepository = refreshTokenFindRepository;
        this.userFindByIdRepository = userFindByIdRepository;
        this.jwtGenerationRepository = jwtGenerationRepository;
    }

    @Override
    public CreateLoginTokenResponse refreshLoginTokens(String token, long userId) {
        log.info(() -> "Refreshing login tokens for user id %s with token %s".formatted(userId, token));

        var refreshToken = refreshTokenFindRepository.findByTokenAndId(token, userId)
                .orElseThrow(()-> new UnauthorizedException("Invalid refresh token! Please login again."));

        var user = userFindByIdRepository.findById(userId);


        return jwtGenerationRepository.generateTokenPair(user, refreshToken);
    }
}
