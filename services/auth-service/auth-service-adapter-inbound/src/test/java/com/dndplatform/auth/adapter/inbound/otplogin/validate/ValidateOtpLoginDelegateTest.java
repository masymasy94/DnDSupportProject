package com.dndplatform.auth.adapter.inbound.otplogin.validate;

import com.dndplatform.auth.adapter.inbound.login.mapper.LoginResponseViewModelMapper;
import com.dndplatform.auth.adapter.inbound.otplogin.validate.mapper.ValidateOtpLoginMapper;
import com.dndplatform.auth.domain.ValidateOtpLoginService;
import com.dndplatform.auth.domain.model.CreateLoginTokenResponse;
import com.dndplatform.auth.domain.model.ValidateOtpLogin;
import com.dndplatform.auth.view.model.vm.CreateLoginTokensResponseViewModel;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ValidateOtpLoginDelegateTest {

    @Mock
    private ValidateOtpLoginMapper mapper;

    @Mock
    private LoginResponseViewModelMapper loginResponseViewModelMapper;

    @Mock
    private ValidateOtpLoginService service;

    private ValidateOtpLoginDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new ValidateOtpLoginDelegate(mapper, loginResponseViewModelMapper, service);
    }

    @Test
    void shouldDelegateToServiceAndReturn201(
            @Random ValidateOtpLoginViewModel viewModel,
            @Random ValidateOtpLogin domainModel,
            @Random CreateLoginTokenResponse serviceResponse,
            @Random CreateLoginTokensResponseViewModel responseViewModel
    ) {
        given(mapper.apply(viewModel)).willReturn(domainModel);
        given(service.validateOtpLogin(domainModel)).willReturn(serviceResponse);
        given(loginResponseViewModelMapper.apply(serviceResponse)).willReturn(responseViewModel);

        Response result = sut.validateOtpLogin(viewModel);

        assertThat(result.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        assertThat(result.getEntity()).isEqualTo(responseViewModel);

        var inOrder = inOrder(mapper, service, loginResponseViewModelMapper);
        then(mapper).should(inOrder).apply(viewModel);
        then(service).should(inOrder).validateOtpLogin(domainModel);
        then(loginResponseViewModelMapper).should(inOrder).apply(serviceResponse);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenOtpIsInvalid(
            @Random ValidateOtpLoginViewModel viewModel,
            @Random ValidateOtpLogin domainModel
    ) {
        given(mapper.apply(viewModel)).willReturn(domainModel);
        given(service.validateOtpLogin(domainModel)).willThrow(new RuntimeException("Invalid OTP code"));

        assertThatThrownBy(() -> sut.validateOtpLogin(viewModel))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid OTP code");
    }
}
