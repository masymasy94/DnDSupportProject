package com.dndplatform.auth.adapter.inbound.logout;

import com.dndplatform.auth.view.model.LogoutResource;
import com.dndplatform.common.test.RandomExtension;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class LogoutResourceImplTest {

    @Mock
    private LogoutResource delegate;

    @Mock
    private Response response;

    private LogoutResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new LogoutResourceImpl(delegate);
    }

    @Test
    void shouldDelegateLogout() {
        String refreshToken = "some-refresh-token";
        long userId = 42L;
        given(delegate.logout(refreshToken, userId)).willReturn(response);

        var result = sut.logout(refreshToken, userId);

        assertThat(result).isEqualTo(response);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).logout(refreshToken, userId);
        inOrder.verifyNoMoreInteractions();
    }
}
