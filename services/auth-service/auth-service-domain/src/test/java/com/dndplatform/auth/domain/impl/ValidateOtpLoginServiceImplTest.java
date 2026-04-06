package com.dndplatform.auth.domain.impl;

import com.dndplatform.auth.domain.JwtGenerationRepository;
import com.dndplatform.auth.domain.model.CreateLoginTokenResponse;
import com.dndplatform.auth.domain.model.OtpLoginToken;
import com.dndplatform.auth.domain.model.RefreshToken;
import com.dndplatform.auth.domain.model.User;
import com.dndplatform.auth.domain.model.ValidateOtpLogin;
import com.dndplatform.auth.domain.repository.OtpLoginTokenFindByTokenRepository;
import com.dndplatform.auth.domain.repository.OtpLoginTokenMarkUsedRepository;
import com.dndplatform.auth.domain.repository.RefreshTokenCreateRepository;
import com.dndplatform.auth.domain.repository.UserFindByEmailRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ValidateOtpLoginServiceImplTest {

    private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2026, 1, 1, 12, 0);
    private final Clock clock = Clock.fixed(FIXED_TIME.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    @Mock
    private UserFindByEmailRepository userFindByEmailRepository;

    @Mock
    private OtpLoginTokenFindByTokenRepository otpLoginTokenFindByTokenRepository;

    @Mock
    private OtpLoginTokenMarkUsedRepository otpLoginTokenMarkUsedRepository;

    @Mock
    private RefreshTokenCreateRepository refreshTokenCreateRepository;

    @Mock
    private JwtGenerationRepository jwtGenerationRepository;

    private ValidateOtpLoginServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ValidateOtpLoginServiceImpl(
                userFindByEmailRepository,
                otpLoginTokenFindByTokenRepository,
                otpLoginTokenMarkUsedRepository,
                refreshTokenCreateRepository,
                jwtGenerationRepository,
                clock
        );
    }

    @Test
    void shouldReturnTokenPairOnSuccessfulOtpValidation(@Random ValidateOtpLogin request,
                                                        @Random User user,
                                                        @Random RefreshToken refreshToken,
                                                        @Random CreateLoginTokenResponse expectedResponse) {
        // Arrange
        var validToken = new OtpLoginToken(1L, request.otpCode(), user.id(), FIXED_TIME.plusHours(1), false, FIXED_TIME);
        given(userFindByEmailRepository.findByEmail(request.email())).willReturn(Optional.of(user));
        given(otpLoginTokenFindByTokenRepository.findByToken(request.otpCode())).willReturn(Optional.of(validToken));
        given(refreshTokenCreateRepository.createRefreshToken(user.id())).willReturn(refreshToken);
        given(jwtGenerationRepository.generateTokenPair(user, refreshToken)).willReturn(expectedResponse);

        // Act
        var result = sut.validateOtpLogin(request);

        // Assert
        assertThat(result).isEqualTo(expectedResponse);

        var inOrder = inOrder(userFindByEmailRepository, otpLoginTokenFindByTokenRepository,
                otpLoginTokenMarkUsedRepository, refreshTokenCreateRepository, jwtGenerationRepository);
        then(userFindByEmailRepository).should(inOrder).findByEmail(request.email());
        then(otpLoginTokenFindByTokenRepository).should(inOrder).findByToken(request.otpCode());
        then(otpLoginTokenMarkUsedRepository).should(inOrder).markUsed(request.otpCode());
        then(refreshTokenCreateRepository).should(inOrder).createRefreshToken(user.id());
        then(jwtGenerationRepository).should(inOrder).generateTokenPair(user, refreshToken);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenUserNotFound(@Random ValidateOtpLogin request) {
        // Arrange
        given(userFindByEmailRepository.findByEmail(request.email())).willReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> sut.validateOtpLogin(request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Invalid email or OTP code");

        then(otpLoginTokenFindByTokenRepository).shouldHaveNoInteractions();
        then(otpLoginTokenMarkUsedRepository).shouldHaveNoInteractions();
        then(refreshTokenCreateRepository).shouldHaveNoInteractions();
        then(jwtGenerationRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenOtpTokenNotFound(@Random ValidateOtpLogin request,
                                                               @Random User user) {
        // Arrange
        given(userFindByEmailRepository.findByEmail(request.email())).willReturn(Optional.of(user));
        given(otpLoginTokenFindByTokenRepository.findByToken(request.otpCode())).willReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> sut.validateOtpLogin(request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Invalid email or OTP code");

        then(otpLoginTokenMarkUsedRepository).shouldHaveNoInteractions();
        then(refreshTokenCreateRepository).shouldHaveNoInteractions();
        then(jwtGenerationRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenUserIdMismatch(@Random ValidateOtpLogin request,
                                                             @Random User user) {
        // Arrange
        var tokenWithDifferentUser = new OtpLoginToken(1L, request.otpCode(), user.id() + 1, FIXED_TIME.plusHours(1), false, FIXED_TIME);
        given(userFindByEmailRepository.findByEmail(request.email())).willReturn(Optional.of(user));
        given(otpLoginTokenFindByTokenRepository.findByToken(request.otpCode())).willReturn(Optional.of(tokenWithDifferentUser));

        // Act & Assert
        assertThatThrownBy(() -> sut.validateOtpLogin(request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Invalid email or OTP code");

        then(otpLoginTokenMarkUsedRepository).shouldHaveNoInteractions();
        then(refreshTokenCreateRepository).shouldHaveNoInteractions();
        then(jwtGenerationRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenOtpAlreadyUsed(@Random ValidateOtpLogin request,
                                                              @Random User user) {
        // Arrange
        var usedToken = new OtpLoginToken(1L, request.otpCode(), user.id(), FIXED_TIME.plusHours(1), true, FIXED_TIME);
        given(userFindByEmailRepository.findByEmail(request.email())).willReturn(Optional.of(user));
        given(otpLoginTokenFindByTokenRepository.findByToken(request.otpCode())).willReturn(Optional.of(usedToken));

        // Act & Assert
        assertThatThrownBy(() -> sut.validateOtpLogin(request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("OTP code has already been used");

        then(otpLoginTokenMarkUsedRepository).shouldHaveNoInteractions();
        then(refreshTokenCreateRepository).shouldHaveNoInteractions();
        then(jwtGenerationRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenOtpExpired(@Random ValidateOtpLogin request,
                                                         @Random User user) {
        // Arrange
        var expiredToken = new OtpLoginToken(1L, request.otpCode(), user.id(), FIXED_TIME.minusHours(1), false, FIXED_TIME);
        given(userFindByEmailRepository.findByEmail(request.email())).willReturn(Optional.of(user));
        given(otpLoginTokenFindByTokenRepository.findByToken(request.otpCode())).willReturn(Optional.of(expiredToken));

        // Act & Assert
        assertThatThrownBy(() -> sut.validateOtpLogin(request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("OTP code has expired");

        then(otpLoginTokenMarkUsedRepository).shouldHaveNoInteractions();
        then(refreshTokenCreateRepository).shouldHaveNoInteractions();
        then(jwtGenerationRepository).shouldHaveNoInteractions();
    }
}
