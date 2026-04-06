package com.dndplatform.auth.adapter.inbound.otplogin.request;

import com.dndplatform.auth.adapter.inbound.otplogin.request.mapper.RequestOtpLoginMapper;
import com.dndplatform.auth.domain.RequestOtpLoginService;
import com.dndplatform.auth.domain.model.RequestOtpLogin;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class RequestOtpLoginDelegateTest {

    @Mock
    private RequestOtpLoginMapper mapper;

    @Mock
    private RequestOtpLoginService service;

    private RequestOtpLoginDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new RequestOtpLoginDelegate(mapper, service);
    }

    @Test
    void shouldDelegateToServiceAndReturnAccepted(
            @Random RequestOtpLoginViewModel viewModel,
            @Random RequestOtpLogin domainModel
    ) {
        given(mapper.apply(viewModel)).willReturn(domainModel);
        willDoNothing().given(service).requestOtpLogin(domainModel);

        Response result = sut.requestOtpLogin(viewModel);

        assertThat(result.getStatus()).isEqualTo(Response.Status.ACCEPTED.getStatusCode());

        var inOrder = inOrder(mapper, service);
        then(mapper).should(inOrder).apply(viewModel);
        then(service).should(inOrder).requestOtpLogin(domainModel);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenServiceThrows(
            @Random RequestOtpLoginViewModel viewModel,
            @Random RequestOtpLogin domainModel
    ) {
        given(mapper.apply(viewModel)).willReturn(domainModel);
        org.mockito.BDDMockito.willThrow(new RuntimeException("Email not found"))
                .given(service).requestOtpLogin(domainModel);

        assertThatThrownBy(() -> sut.requestOtpLogin(viewModel))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Email not found");
    }
}
