package com.dndplatform.auth.adapter.inbound.passwordreset.request;

import com.dndplatform.auth.adapter.inbound.passwordreset.request.mapper.RequestPasswordResetMapper;
import com.dndplatform.auth.domain.RequestPasswordResetService;
import com.dndplatform.auth.domain.model.RequestPasswordReset;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class RequestPasswordResetDelegateTest {

    @Mock
    private RequestPasswordResetMapper mapper;

    @Mock
    private RequestPasswordResetService service;

    private RequestPasswordResetDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new RequestPasswordResetDelegate(mapper, service);
    }

    @Test
    void shouldDelegateToServiceAndReturnAccepted(
            @Random RequestPasswordResetViewModel viewModel,
            @Random RequestPasswordReset domainModel
    ) {
        given(mapper.apply(viewModel)).willReturn(domainModel);
        willDoNothing().given(service).requestPasswordReset(domainModel);

        Response result = sut.requestPasswordReset(viewModel);

        assertThat(result.getStatus()).isEqualTo(Response.Status.ACCEPTED.getStatusCode());

        var inOrder = inOrder(mapper, service);
        then(mapper).should(inOrder).apply(viewModel);
        then(service).should(inOrder).requestPasswordReset(domainModel);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenServiceThrows(
            @Random RequestPasswordResetViewModel viewModel,
            @Random RequestPasswordReset domainModel
    ) {
        given(mapper.apply(viewModel)).willReturn(domainModel);
        willThrow(new RuntimeException("Email not registered")).given(service).requestPasswordReset(domainModel);

        assertThatThrownBy(() -> sut.requestPasswordReset(viewModel))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Email not registered");
    }
}
