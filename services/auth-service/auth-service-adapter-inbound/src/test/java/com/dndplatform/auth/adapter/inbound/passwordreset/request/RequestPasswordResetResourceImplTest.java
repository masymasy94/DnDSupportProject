package com.dndplatform.auth.adapter.inbound.passwordreset.request;

import com.dndplatform.auth.view.model.RequestPasswordResetResource;
import com.dndplatform.auth.view.model.vm.RequestPasswordResetViewModel;
import com.dndplatform.common.test.Random;
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
class RequestPasswordResetResourceImplTest {

    @Mock
    private RequestPasswordResetResource delegate;

    @Mock
    private Response response;

    private RequestPasswordResetResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new RequestPasswordResetResourceImpl(delegate);
    }

    @Test
    void shouldDelegateRequestPasswordReset(@Random RequestPasswordResetViewModel request) {
        given(delegate.requestPasswordReset(request)).willReturn(response);

        var result = sut.requestPasswordReset(request);

        assertThat(result).isEqualTo(response);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).requestPasswordReset(request);
        inOrder.verifyNoMoreInteractions();
    }
}
