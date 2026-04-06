package com.dndplatform.auth.adapter.inbound.logout;

import com.dndplatform.auth.domain.LogoutService;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class LogoutDelegateTest {

    @Mock
    private LogoutService logoutService;

    private LogoutDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new LogoutDelegate(logoutService);
    }

    @Test
    void shouldDelegateToServiceAndReturn204(@Random String refreshToken) {
        long userId = 42L;
        willDoNothing().given(logoutService).logout(refreshToken, userId);

        Response result = sut.logout(refreshToken, userId);

        assertThat(result.getStatus()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());

        var inOrder = inOrder(logoutService);
        then(logoutService).should(inOrder).logout(refreshToken, userId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenServiceThrows(@Random String refreshToken) {
        long userId = 42L;
        willThrow(new RuntimeException("Token not found")).given(logoutService).logout(refreshToken, userId);

        assertThatThrownBy(() -> sut.logout(refreshToken, userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Token not found");
    }
}
