package com.dndplatform.auth.adapter.inbound.otplogin.validate;

import com.dndplatform.auth.view.model.ValidateOtpLoginResource;
import com.dndplatform.auth.view.model.vm.ValidateOtpLoginViewModel;
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
class ValidateOtpLoginResourceImplTest {

    @Mock
    private ValidateOtpLoginResource delegate;

    @Mock
    private Response response;

    private ValidateOtpLoginResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ValidateOtpLoginResourceImpl(delegate);
    }

    @Test
    void shouldDelegateValidateOtpLogin(@Random ValidateOtpLoginViewModel request) {
        given(delegate.validateOtpLogin(request)).willReturn(response);

        var result = sut.validateOtpLogin(request);

        assertThat(result).isEqualTo(response);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).validateOtpLogin(request);
        inOrder.verifyNoMoreInteractions();
    }
}
