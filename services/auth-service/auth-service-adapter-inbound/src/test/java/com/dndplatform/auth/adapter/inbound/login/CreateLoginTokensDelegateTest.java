package com.dndplatform.auth.adapter.inbound.login;

import com.dndplatform.auth.adapter.inbound.login.mapper.CreateLoginTokensMapper;
import com.dndplatform.auth.adapter.inbound.login.mapper.LoginResponseViewModelMapper;
import com.dndplatform.auth.domain.CreateLoginTokensService;
import com.dndplatform.auth.domain.model.CreateLoginTokenResponse;
import com.dndplatform.auth.domain.model.CreateLoginTokens;
import com.dndplatform.auth.view.model.vm.CreateLoginTokensResponseViewModel;
import com.dndplatform.auth.view.model.vm.CreateLoginTokensViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CreateLoginTokensDelegateTest {

    @Mock
    private CreateLoginTokensMapper createLoginTokensMapper;

    @Mock
    private LoginResponseViewModelMapper loginResponseViewModelMapper;

    @Mock
    private CreateLoginTokensService service;

    private CreateLoginTokensDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CreateLoginTokensDelegate(createLoginTokensMapper, loginResponseViewModelMapper, service);
    }

    @Test
    void shouldDelegateToServiceAndReturn201(
            @Random CreateLoginTokensViewModel viewModel,
            @Random CreateLoginTokens domainModel,
            @Random CreateLoginTokenResponse serviceResponse,
            @Random CreateLoginTokensResponseViewModel responseViewModel
    ) {
        given(createLoginTokensMapper.apply(viewModel)).willReturn(domainModel);
        given(service.createLoginTokens(any(CreateLoginTokens.class))).willReturn(serviceResponse);
        given(loginResponseViewModelMapper.apply(serviceResponse)).willReturn(responseViewModel);

        Response result = sut.createLoginTokens(viewModel);

        assertThat(result.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        assertThat(result.getEntity()).isEqualTo(responseViewModel);

        var inOrder = inOrder(createLoginTokensMapper, service, loginResponseViewModelMapper);
        then(createLoginTokensMapper).should(inOrder).apply(viewModel);
        then(service).should(inOrder).createLoginTokens(any(CreateLoginTokens.class));
        then(loginResponseViewModelMapper).should(inOrder).apply(serviceResponse);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenServiceThrows(@Random CreateLoginTokensViewModel viewModel,
                                       @Random CreateLoginTokens domainModel) {
        given(createLoginTokensMapper.apply(viewModel)).willReturn(domainModel);
        given(service.createLoginTokens(any(CreateLoginTokens.class))).willThrow(new RuntimeException("Invalid credentials"));

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> sut.createLoginTokens(viewModel))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid credentials");

        var inOrder = inOrder(createLoginTokensMapper, service, loginResponseViewModelMapper);
        then(createLoginTokensMapper).should(inOrder).apply(viewModel);
        then(service).should(inOrder).createLoginTokens(any(CreateLoginTokens.class));
        inOrder.verifyNoMoreInteractions();
    }
}
