package com.dndplatform.auth.domain.impl;

import com.dndplatform.auth.domain.UserLoginService;
import com.dndplatform.auth.domain.model.TokenPair;
import com.dndplatform.auth.domain.model.UserLogin;
import com.dndplatform.auth.domain.repository.RefreshTokenCreateRepository;
import com.dndplatform.auth.domain.repository.UserCredentialsValidateRepository;
import com.dndplatform.auth.domain.service.JwtTokenService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserLoginServiceImpl implements UserLoginService {

    private final UserCredentialsValidateRepository userCredentialsValidateRepository;
    private final RefreshTokenCreateRepository refreshTokenCreateRepository;
    private final JwtTokenService jwtTokenService;

    @Inject
    public UserLoginServiceImpl(UserCredentialsValidateRepository userCredentialsValidateRepository,
                                RefreshTokenCreateRepository refreshTokenCreateRepository,
                                JwtTokenService jwtTokenService) {
        this.userCredentialsValidateRepository = userCredentialsValidateRepository;
        this.refreshTokenCreateRepository = refreshTokenCreateRepository;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public TokenPair login(UserLogin userLogin) {

        var user = userCredentialsValidateRepository.validateCredentials(
                userLogin.username(),
                userLogin.password()
        );

        var refreshToken = refreshTokenCreateRepository.createRefreshToken(user.id());

        return jwtTokenService.generateTokenPair(user, refreshToken);
    }
}
