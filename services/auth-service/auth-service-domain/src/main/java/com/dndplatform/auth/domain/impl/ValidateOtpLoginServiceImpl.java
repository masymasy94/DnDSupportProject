package com.dndplatform.auth.domain.impl;

import com.dndplatform.auth.domain.JwtGenerationRepository;
import com.dndplatform.auth.domain.ValidateOtpLoginService;
import com.dndplatform.auth.domain.model.CreateLoginTokenResponse;
import com.dndplatform.auth.domain.model.OtpLoginToken;
import com.dndplatform.auth.domain.model.ValidateOtpLogin;
import com.dndplatform.auth.domain.repository.OtpLoginTokenFindByTokenRepository;
import com.dndplatform.auth.domain.repository.OtpLoginTokenMarkUsedRepository;
import com.dndplatform.auth.domain.repository.RefreshTokenCreateRepository;
import com.dndplatform.auth.domain.repository.UserFindByEmailRepository;
import com.dndplatform.common.exception.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
public class ValidateOtpLoginServiceImpl implements ValidateOtpLoginService {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final UserFindByEmailRepository userFindByEmailRepository;
    private final OtpLoginTokenFindByTokenRepository otpLoginTokenFindByTokenRepository;
    private final OtpLoginTokenMarkUsedRepository otpLoginTokenMarkUsedRepository;
    private final RefreshTokenCreateRepository refreshTokenCreateRepository;
    private final JwtGenerationRepository jwtGenerationRepository;

    @Inject
    public ValidateOtpLoginServiceImpl(UserFindByEmailRepository userFindByEmailRepository,
                                       OtpLoginTokenFindByTokenRepository otpLoginTokenFindByTokenRepository,
                                       OtpLoginTokenMarkUsedRepository otpLoginTokenMarkUsedRepository,
                                       RefreshTokenCreateRepository refreshTokenCreateRepository,
                                       JwtGenerationRepository jwtGenerationRepository) {
        this.userFindByEmailRepository = userFindByEmailRepository;
        this.otpLoginTokenFindByTokenRepository = otpLoginTokenFindByTokenRepository;
        this.otpLoginTokenMarkUsedRepository = otpLoginTokenMarkUsedRepository;
        this.refreshTokenCreateRepository = refreshTokenCreateRepository;
        this.jwtGenerationRepository = jwtGenerationRepository;
    }

    @Override
    public CreateLoginTokenResponse validateOtpLogin(ValidateOtpLogin request) {
        log.info(() -> "Validating OTP login for email: %s".formatted(request.email()));

        var user = userFindByEmailRepository.findByEmail(request.email())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or OTP code"));

        var token = otpLoginTokenFindByTokenRepository.findByToken(request.otpCode())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or OTP code"));

        validateToken(token, user.id());

        otpLoginTokenMarkUsedRepository.markUsed(request.otpCode());

        var refreshToken = refreshTokenCreateRepository.createRefreshToken(user.id());
        return jwtGenerationRepository.generateTokenPair(user, refreshToken);
    }

    private static void validateToken(OtpLoginToken token, Long userId) {
        if (!token.userId().equals(userId)) {
            throw new UnauthorizedException("Invalid email or OTP code");
        }
        if (Boolean.TRUE.equals(token.used())) {
            throw new UnauthorizedException("OTP code has already been used");
        }
        if (token.expiresAt().isBefore(LocalDateTime.now())) {
            throw new UnauthorizedException("OTP code has expired");
        }
    }
}
