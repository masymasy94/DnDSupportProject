package com.dndplatform.auth.adapter.inbound.otplogin.request;

import com.dndplatform.auth.view.model.RequestOtpLoginResource;
import com.dndplatform.auth.view.model.vm.RequestOtpLoginViewModel;
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
class RequestOtpLoginResourceImplTest {

    @Mock
    private RequestOtpLoginResource delegate;

    @Mock
    private Response response;

    private RequestOtpLoginResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new RequestOtpLoginResourceImpl(delegate);
    }

    @Test
    void shouldDelegateRequestOtpLogin(@Random RequestOtpLoginViewModel request) {
        given(delegate.requestOtpLogin(request)).willReturn(response);

        var result = sut.requestOtpLogin(request);

        assertThat(result).isEqualTo(response);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).requestOtpLogin(request);
        inOrder.verifyNoMoreInteractions();
    }
}
