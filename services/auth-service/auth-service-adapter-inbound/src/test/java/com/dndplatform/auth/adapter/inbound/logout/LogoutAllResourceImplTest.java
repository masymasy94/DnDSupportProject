package com.dndplatform.auth.adapter.inbound.logout;

import com.dndplatform.auth.view.model.LogoutAllResource;
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
class LogoutAllResourceImplTest {

    @Mock
    private LogoutAllResource delegate;

    @Mock
    private Response response;

    private LogoutAllResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new LogoutAllResourceImpl(delegate);
    }

    @Test
    void shouldDelegateLogoutAll() {
        long userId = 7L;
        given(delegate.logoutAll(userId)).willReturn(response);

        var result = sut.logoutAll(userId);

        assertThat(result).isEqualTo(response);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).logoutAll(userId);
        inOrder.verifyNoMoreInteractions();
    }
}
