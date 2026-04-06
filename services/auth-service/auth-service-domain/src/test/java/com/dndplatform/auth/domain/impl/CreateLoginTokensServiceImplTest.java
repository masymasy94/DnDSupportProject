package com.dndplatform.auth.domain.impl;

import com.dndplatform.auth.domain.JwtGenerationRepository;
import com.dndplatform.auth.domain.model.CreateLoginTokenResponse;
import com.dndplatform.auth.domain.model.CreateLoginTokens;
import com.dndplatform.auth.domain.model.RefreshToken;
import com.dndplatform.auth.domain.model.User;
import com.dndplatform.auth.domain.repository.RefreshTokenCreateRepository;
import com.dndplatform.auth.domain.repository.UserCredentialsValidateRepository;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.UnauthorizedException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CreateLoginTokensServiceImplTest {

    @Mock
    private UserCredentialsValidateRepository userCredentialsValidateRepository;

    @Mock
    private RefreshTokenCreateRepository refreshTokenCreateRepository;

    @Mock
    private JwtGenerationRepository jwtTokenService;

    private CreateLoginTokensServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CreateLoginTokensServiceImpl(
                userCredentialsValidateRepository,
                refreshTokenCreateRepository,
                jwtTokenService
        );
    }

    @Test
    void shouldReturnTokenPairOnSuccessfulValidation(@Random CreateLoginTokens request,
                                                     @Random User user,
                                                     @Random RefreshToken refreshToken,
                                                     @Random CreateLoginTokenResponse expectedResponse) {
        // Arrange
        given(userCredentialsValidateRepository.validateCredentials(request.username(), request.password())).willReturn(user);
        given(refreshTokenCreateRepository.createRefreshToken(user.id())).willReturn(refreshToken);
        given(jwtTokenService.generateTokenPair(user, refreshToken)).willReturn(expectedResponse);

        // Act
        var result = sut.createLoginTokens(request);

        // Assert
        assertThat(result).isEqualTo(expectedResponse);

        var inOrder = inOrder(userCredentialsValidateRepository, refreshTokenCreateRepository, jwtTokenService);
        then(userCredentialsValidateRepository).should(inOrder).validateCredentials(request.username(), request.password());
        then(refreshTokenCreateRepository).should(inOrder).createRefreshToken(user.id());
        then(jwtTokenService).should(inOrder).generateTokenPair(user, refreshToken);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenCredentialsInvalid(@Random CreateLoginTokens request) {
        // Arrange
        given(userCredentialsValidateRepository.validateCredentials(request.username(), request.password()))
                .willThrow(new UnauthorizedException("Invalid credentials for username " + request.username()));

        // Act & Assert
        assertThatThrownBy(() -> sut.createLoginTokens(request))
                .isInstanceOf(UnauthorizedException.class);

        then(refreshTokenCreateRepository).shouldHaveNoInteractions();
        then(jwtTokenService).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowForbiddenExceptionWhenUserNotActive(@Random CreateLoginTokens request) {
        // Arrange
        given(userCredentialsValidateRepository.validateCredentials(request.username(), request.password()))
                .willThrow(new ForbiddenException("User is not active"));

        // Act & Assert
        assertThatThrownBy(() -> sut.createLoginTokens(request))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("User is not active");

        then(refreshTokenCreateRepository).shouldHaveNoInteractions();
        then(jwtTokenService).shouldHaveNoInteractions();
    }
}
