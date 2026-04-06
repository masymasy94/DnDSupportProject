package com.dndplatform.auth.domain.impl;

import com.dndplatform.auth.domain.repository.RefreshTokenRevokeRepository;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class LogoutServiceImplTest {

    @Mock
    private RefreshTokenRevokeRepository refreshTokenRevokeRepository;

    private LogoutServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new LogoutServiceImpl(refreshTokenRevokeRepository);
    }

    @Test
    void shouldRevokeTokenOnLogout(@Random String token, @Random Long userId) {
        // Act
        sut.logout(token, userId);

        // Assert
        var inOrder = inOrder(refreshTokenRevokeRepository);
        then(refreshTokenRevokeRepository).should(inOrder).revokeToken(token, userId);
        inOrder.verifyNoMoreInteractions();
    }
}
