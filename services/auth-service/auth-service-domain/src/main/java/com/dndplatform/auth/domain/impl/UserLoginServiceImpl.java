package com.dndplatform.auth.domain.impl;

import com.dndplatform.auth.domain.model.TokenPair;
import com.dndplatform.auth.domain.repository.RefreshTokenCreateRepository;
import com.dndplatform.auth.domain.UserLoginService;
import com.dndplatform.auth.domain.repository.UserUpdateLastLoginByIdRepository;
import com.dndplatform.auth.domain.model.User;
import com.dndplatform.auth.domain.model.UserLogin;
import com.dndplatform.auth.domain.repository.UserFindByCredentialsRepository;
import com.dndplatform.auth.domain.service.JwtTokenService;
import com.dndplatform.auth.domain.util.CryptUtil;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.UnauthorizedException;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserLoginServiceImpl implements UserLoginService {

    private final UserFindByCredentialsRepository userFindByCredentialsRepository;
    private final UserUpdateLastLoginByIdRepository userUpdateLastLoginByIdRepository;
    private final RefreshTokenCreateRepository refreshTokenCreateRepository;
    private final JwtTokenService jwtTokenService;

    @Inject
    public UserLoginServiceImpl(UserFindByCredentialsRepository userFindByCredentialsRepository,
                                UserUpdateLastLoginByIdRepository userUpdateLastLoginByIdRepository,
                                RefreshTokenCreateRepository refreshTokenCreateRepository,
                                JwtTokenService jwtTokenService) {
        this.userFindByCredentialsRepository = userFindByCredentialsRepository;
        this.userUpdateLastLoginByIdRepository = userUpdateLastLoginByIdRepository;
        this.refreshTokenCreateRepository = refreshTokenCreateRepository;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public TokenPair login(UserLogin userLogin) {

        var user = getUserByCredentials(userLogin);

        userUpdateLastLoginByIdRepository.updateLastLoginById(user.id());

        var refreshToken = refreshTokenCreateRepository.createRefreshToken(user.id());

        return jwtTokenService.generateTokenPair(user, refreshToken);
    }


    @Nonnull
    private User getUserByCredentials(UserLogin userLogin) {
        var passwordHash = CryptUtil.hashPassword(userLogin.password());
        var user = userFindByCredentialsRepository.findUserByCredentials(userLogin.username(), passwordHash)
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!user.active())
            throw new ForbiddenException("User account is not active");

        return user;
    }
}
