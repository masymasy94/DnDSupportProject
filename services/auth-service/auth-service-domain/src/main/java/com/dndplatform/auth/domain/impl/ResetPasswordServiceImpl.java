package com.dndplatform.auth.domain.impl;

import com.dndplatform.auth.domain.ResetPasswordService;
import com.dndplatform.auth.domain.model.PasswordResetToken;
import com.dndplatform.auth.domain.model.ResetPassword;
import com.dndplatform.auth.domain.repository.PasswordResetTokenFindByTokenRepository;
import com.dndplatform.auth.domain.repository.PasswordResetTokenMarkUsedRepository;
import com.dndplatform.auth.domain.repository.RefreshTokenRevokeAllRepository;
import com.dndplatform.auth.domain.repository.UserUpdatePasswordRepository;
import com.dndplatform.common.exception.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final PasswordResetTokenFindByTokenRepository passwordResetTokenFindByTokenRepository;
    private final PasswordResetTokenMarkUsedRepository passwordResetTokenMarkUsedRepository;
    private final UserUpdatePasswordRepository userUpdatePasswordRepository;
    private final RefreshTokenRevokeAllRepository refreshTokenRevokeAllRepository;

    @Inject
    public ResetPasswordServiceImpl(PasswordResetTokenFindByTokenRepository passwordResetTokenFindByTokenRepository,
                                    PasswordResetTokenMarkUsedRepository passwordResetTokenMarkUsedRepository,
                                    UserUpdatePasswordRepository userUpdatePasswordRepository,
                                    RefreshTokenRevokeAllRepository refreshTokenRevokeAllRepository) {
        this.passwordResetTokenFindByTokenRepository = passwordResetTokenFindByTokenRepository;
        this.passwordResetTokenMarkUsedRepository = passwordResetTokenMarkUsedRepository;
        this.userUpdatePasswordRepository = userUpdatePasswordRepository;
        this.refreshTokenRevokeAllRepository = refreshTokenRevokeAllRepository;
    }

    @Override
    public void resetPassword(ResetPassword request) {
        log.info("Processing password reset token");

        var token = passwordResetTokenFindByTokenRepository.findByToken(request.token())
                .orElseThrow(() -> new UnauthorizedException("Invalid or expired password reset token"));

        validateToken(token);

        userUpdatePasswordRepository.updatePassword(token.userId(), request.newPassword());
        passwordResetTokenMarkUsedRepository.markUsed(request.token());
        refreshTokenRevokeAllRepository.revokeAllTokens(token.userId());

        log.info(() -> "Password successfully reset for user id: %s".formatted(token.userId()));
    }

    private static void validateToken(PasswordResetToken token) {
        if (Boolean.TRUE.equals(token.used())) {
            throw new UnauthorizedException("Password reset token has already been used");
        }
        if (token.expiresAt().isBefore(LocalDateTime.now())) {
            throw new UnauthorizedException("Password reset token has expired");
        }
    }
}
