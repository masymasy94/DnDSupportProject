package com.dndplatform.auth.adapter.inbound.passwordreset.reset;

import com.dndplatform.auth.view.model.ResetPasswordResource;
import com.dndplatform.auth.view.model.vm.ResetPasswordViewModel;
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
class ResetPasswordResourceImplTest {

    @Mock
    private ResetPasswordResource delegate;

    @Mock
    private Response response;

    private ResetPasswordResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ResetPasswordResourceImpl(delegate);
    }

    @Test
    void shouldDelegateResetPassword(@Random ResetPasswordViewModel request) {
        given(delegate.resetPassword(request)).willReturn(response);

        var result = sut.resetPassword(request);

        assertThat(result).isEqualTo(response);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).resetPassword(request);
        inOrder.verifyNoMoreInteractions();
    }
}
