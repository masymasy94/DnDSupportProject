package com.dndplatform.auth.domain.impl;

import com.dndplatform.auth.domain.CreateLoginTokensService;
import com.dndplatform.auth.domain.model.CreateLoginTokenResponse;
import com.dndplatform.auth.domain.model.CreateLoginTokens;
import com.dndplatform.auth.domain.repository.RefreshTokenCreateRepository;
import com.dndplatform.auth.domain.repository.UserCredentialsValidateRepository;
import com.dndplatform.auth.domain.service.JwtTokenService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CreateLoginTokensServiceImpl implements CreateLoginTokensService {

    private final UserCredentialsValidateRepository userCredentialsValidateRepository;
    private final RefreshTokenCreateRepository refreshTokenCreateRepository;
    private final JwtTokenService jwtTokenService;

    @Inject
    public CreateLoginTokensServiceImpl(UserCredentialsValidateRepository userCredentialsValidateRepository, RefreshTokenCreateRepository refreshTokenCreateRepository, JwtTokenService jwtTokenService) {
        this.userCredentialsValidateRepository = userCredentialsValidateRepository;
        this.refreshTokenCreateRepository = refreshTokenCreateRepository;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public CreateLoginTokenResponse createLoginTokens(CreateLoginTokens createLoginTokens) {

        var user = userCredentialsValidateRepository.validateCredentials(createLoginTokens.username(), createLoginTokens.password());
        var refreshToken = refreshTokenCreateRepository.createRefreshToken(user.id());
        return jwtTokenService.generateTokenPair(user, refreshToken);

        // todo - notification for new device login - queue email/SMS
        // todo - log login attempt, successful or not
        // todo - handle failed login attempts (lock account, captcha, etc)
        // todo - consider adding multi-factor authentication (MFA) support

    }
}
