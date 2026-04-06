package com.dndplatform.auth.adapter.inbound.logout;

import com.dndplatform.auth.domain.LogoutAllService;
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
class LogoutAllDelegateTest {

    @Mock
    private LogoutAllService logoutAllService;

    private LogoutAllDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new LogoutAllDelegate(logoutAllService);
    }

    @Test
    void shouldDelegateToServiceAndReturn204() {
        long userId = 7L;
        willDoNothing().given(logoutAllService).logoutAll(userId);

        Response result = sut.logoutAll(userId);

        assertThat(result.getStatus()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());

        var inOrder = inOrder(logoutAllService);
        then(logoutAllService).should(inOrder).logoutAll(userId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenServiceThrows() {
        long userId = 7L;
        willThrow(new RuntimeException("User not found")).given(logoutAllService).logoutAll(userId);

        assertThatThrownBy(() -> sut.logoutAll(userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }
}
