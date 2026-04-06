package com.dndplatform.auth.domain.impl;

import com.dndplatform.auth.domain.JwtGenerationRepository;
import com.dndplatform.auth.domain.model.CreateLoginTokenResponse;
import com.dndplatform.auth.domain.model.RefreshToken;
import com.dndplatform.auth.domain.model.User;
import com.dndplatform.auth.domain.repository.RefreshTokenFindByTokenAndIdRepository;
import com.dndplatform.auth.domain.repository.UserFindByIdRepository;
import com.dndplatform.common.exception.UnauthorizedException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class RefreshLoginTokensServiceImplTest {

    @Mock
    private RefreshTokenFindByTokenAndIdRepository refreshTokenFindRepository;

    @Mock
    private UserFindByIdRepository userFindByIdRepository;

    @Mock
    private JwtGenerationRepository jwtGenerationRepository;

    private RefreshLoginTokensServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new RefreshLoginTokensServiceImpl(
                refreshTokenFindRepository,
                userFindByIdRepository,
                jwtGenerationRepository
        );
    }

    @Test
    void shouldReturnNewTokenPairOnSuccessfulRefresh(@Random String token,
                                                     @Random Long userId,
                                                     @Random RefreshToken refreshToken,
                                                     @Random User user,
                                                     @Random CreateLoginTokenResponse expectedResponse) {
        // Arrange
        given(refreshTokenFindRepository.findByTokenAndId(token, userId)).willReturn(Optional.of(refreshToken));
        given(userFindByIdRepository.findById(userId)).willReturn(Optional.of(user));
        given(jwtGenerationRepository.generateTokenPair(user, refreshToken)).willReturn(expectedResponse);

        // Act
        var result = sut.refreshLoginTokens(token, userId);

        // Assert
        assertThat(result).isEqualTo(expectedResponse);

        var inOrder = inOrder(refreshTokenFindRepository, userFindByIdRepository, jwtGenerationRepository);
        then(refreshTokenFindRepository).should(inOrder).findByTokenAndId(token, userId);
        then(userFindByIdRepository).should(inOrder).findById(userId);
        then(jwtGenerationRepository).should(inOrder).generateTokenPair(user, refreshToken);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenTokenNotFound(@Random String token, @Random Long userId) {
        // Arrange
        given(refreshTokenFindRepository.findByTokenAndId(token, userId)).willReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> sut.refreshLoginTokens(token, userId))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Invalid refresh token! Please login again.");

        then(userFindByIdRepository).shouldHaveNoInteractions();
        then(jwtGenerationRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenUserNotFound(@Random String token,
                                                          @Random Long userId,
                                                          @Random RefreshToken refreshToken) {
        // Arrange
        given(refreshTokenFindRepository.findByTokenAndId(token, userId)).willReturn(Optional.of(refreshToken));
        given(userFindByIdRepository.findById(userId)).willReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> sut.refreshLoginTokens(token, userId))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Invalid refresh token! Please login again.");

        then(jwtGenerationRepository).shouldHaveNoInteractions();
    }
}
