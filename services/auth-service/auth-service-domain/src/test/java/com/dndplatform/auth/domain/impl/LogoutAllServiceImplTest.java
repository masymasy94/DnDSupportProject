package com.dndplatform.auth.domain.impl;

import com.dndplatform.auth.domain.repository.RefreshTokenRevokeAllRepository;
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
class LogoutAllServiceImplTest {

    @Mock
    private RefreshTokenRevokeAllRepository refreshTokenRevokeAllRepository;

    private LogoutAllServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new LogoutAllServiceImpl(refreshTokenRevokeAllRepository);
    }

    @Test
    void shouldRevokeAllTokensOnLogoutAll(@Random Long userId) {
        // Act
        sut.logoutAll(userId);

        // Assert
        var inOrder = inOrder(refreshTokenRevokeAllRepository);
        then(refreshTokenRevokeAllRepository).should(inOrder).revokeAllTokens(userId);
        inOrder.verifyNoMoreInteractions();
    }
}
