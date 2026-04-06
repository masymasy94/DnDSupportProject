package com.dndplatform.auth.adapter.inbound.refresh;

import com.dndplatform.auth.adapter.inbound.refresh.mapper.RefreshLoginTokenResponseViewModelMapper;
import com.dndplatform.auth.domain.RefreshLoginTokensService;
import com.dndplatform.auth.domain.model.CreateLoginTokenResponse;
import com.dndplatform.auth.view.model.vm.RefreshLoginTokenResponseViewModel;
import com.dndplatform.auth.view.model.vm.RefreshTokenViewModel;
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
class RefreshLoginTokensDelegateTest {

    @Mock
    private RefreshLoginTokensService service;

    @Mock
    private RefreshLoginTokenResponseViewModelMapper mapper;

    private RefreshLoginTokensDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new RefreshLoginTokensDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToServiceAndReturn201(
            @Random CreateLoginTokenResponse serviceResponse,
            @Random RefreshLoginTokenResponseViewModel responseViewModel
    ) {
        var viewModel = new RefreshTokenViewModel("some-refresh-token", 1L);
        given(service.refreshLoginTokens(viewModel.token(), viewModel.userId())).willReturn(serviceResponse);
        given(mapper.apply(serviceResponse)).willReturn(responseViewModel);

        Response result = sut.refreshLoginTokens(viewModel);

        assertThat(result.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        assertThat(result.getEntity()).isEqualTo(responseViewModel);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).refreshLoginTokens(viewModel.token(), viewModel.userId());
        then(mapper).should(inOrder).apply(serviceResponse);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenServiceThrows() {
        var viewModel = new RefreshTokenViewModel("expired-token", 2L);
        given(service.refreshLoginTokens(viewModel.token(), viewModel.userId()))
                .willThrow(new RuntimeException("Token expired"));

        assertThatThrownBy(() -> sut.refreshLoginTokens(viewModel))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Token expired");
    }
}
