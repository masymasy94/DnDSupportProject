package com.dndplatform.auth.domain.impl;

import com.dndplatform.auth.domain.model.PasswordResetToken;
import com.dndplatform.auth.domain.model.ResetPassword;
import com.dndplatform.auth.domain.repository.PasswordResetTokenFindByTokenRepository;
import com.dndplatform.auth.domain.repository.PasswordResetTokenMarkUsedRepository;
import com.dndplatform.auth.domain.repository.RefreshTokenRevokeAllRepository;
import com.dndplatform.auth.domain.repository.UserUpdatePasswordRepository;
import com.dndplatform.common.exception.UnauthorizedException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ResetPasswordServiceImplTest {

    private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2026, 1, 1, 12, 0);
    private final Clock clock = Clock.fixed(FIXED_TIME.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    @Mock
    private PasswordResetTokenFindByTokenRepository passwordResetTokenFindByTokenRepository;

    @Mock
    private PasswordResetTokenMarkUsedRepository passwordResetTokenMarkUsedRepository;

    @Mock
    private UserUpdatePasswordRepository userUpdatePasswordRepository;

    @Mock
    private RefreshTokenRevokeAllRepository refreshTokenRevokeAllRepository;

    private ResetPasswordServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ResetPasswordServiceImpl(
                passwordResetTokenFindByTokenRepository,
                passwordResetTokenMarkUsedRepository,
                userUpdatePasswordRepository,
                refreshTokenRevokeAllRepository,
                clock
        );
    }

    @Test
    void shouldSuccessfullyResetPasswordWhenTokenValid(@Random ResetPassword request, @Random Long userId) {
        // Arrange
        var validToken = new PasswordResetToken(1L, request.token(), userId, FIXED_TIME.plusHours(1), false, FIXED_TIME);
        given(passwordResetTokenFindByTokenRepository.findByToken(request.token())).willReturn(Optional.of(validToken));

        // Act
        sut.resetPassword(request);

        // Assert
        var inOrder = inOrder(passwordResetTokenFindByTokenRepository, userUpdatePasswordRepository,
                passwordResetTokenMarkUsedRepository, refreshTokenRevokeAllRepository);
        then(passwordResetTokenFindByTokenRepository).should(inOrder).findByToken(request.token());
        then(userUpdatePasswordRepository).should(inOrder).updatePassword(userId, request.newPassword());
        then(passwordResetTokenMarkUsedRepository).should(inOrder).markUsed(request.token());
        then(refreshTokenRevokeAllRepository).should(inOrder).revokeAllTokens(userId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenTokenNotFound(@Random ResetPassword request) {
        // Arrange
        given(passwordResetTokenFindByTokenRepository.findByToken(request.token())).willReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> sut.resetPassword(request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Invalid or expired password reset token");

        then(userUpdatePasswordRepository).shouldHaveNoInteractions();
        then(passwordResetTokenMarkUsedRepository).shouldHaveNoInteractions();
        then(refreshTokenRevokeAllRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenTokenAlreadyUsed(@Random ResetPassword request, @Random Long userId) {
        // Arrange
        var usedToken = new PasswordResetToken(1L, request.token(), userId, FIXED_TIME.plusHours(1), true, FIXED_TIME);
        given(passwordResetTokenFindByTokenRepository.findByToken(request.token())).willReturn(Optional.of(usedToken));

        // Act & Assert
        assertThatThrownBy(() -> sut.resetPassword(request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Password reset token has already been used");

        then(userUpdatePasswordRepository).shouldHaveNoInteractions();
        then(passwordResetTokenMarkUsedRepository).shouldHaveNoInteractions();
        then(refreshTokenRevokeAllRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenTokenExpired(@Random ResetPassword request, @Random Long userId) {
        // Arrange
        var expiredToken = new PasswordResetToken(1L, request.token(), userId, FIXED_TIME.minusHours(1), false, FIXED_TIME);
        given(passwordResetTokenFindByTokenRepository.findByToken(request.token())).willReturn(Optional.of(expiredToken));

        // Act & Assert
        assertThatThrownBy(() -> sut.resetPassword(request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Password reset token has expired");

        then(userUpdatePasswordRepository).shouldHaveNoInteractions();
        then(passwordResetTokenMarkUsedRepository).shouldHaveNoInteractions();
        then(refreshTokenRevokeAllRepository).shouldHaveNoInteractions();
    }
}
