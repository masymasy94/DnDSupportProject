package com.dndplatform.auth.adapter.inbound.passwordreset.reset;

import com.dndplatform.auth.adapter.inbound.passwordreset.reset.mapper.ResetPasswordMapper;
import com.dndplatform.auth.domain.ResetPasswordService;
import com.dndplatform.auth.domain.model.ResetPassword;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ResetPasswordDelegateTest {

    @Mock
    private ResetPasswordMapper mapper;

    @Mock
    private ResetPasswordService service;

    private ResetPasswordDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new ResetPasswordDelegate(mapper, service);
    }

    @Test
    void shouldDelegateToServiceAndReturn204(
            @Random ResetPasswordViewModel viewModel,
            @Random ResetPassword domainModel
    ) {
        given(mapper.apply(viewModel)).willReturn(domainModel);
        willDoNothing().given(service).resetPassword(domainModel);

        Response result = sut.resetPassword(viewModel);

        assertThat(result.getStatus()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());

        var inOrder = inOrder(mapper, service);
        then(mapper).should(inOrder).apply(viewModel);
        then(service).should(inOrder).resetPassword(domainModel);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenTokenIsInvalid(
            @Random ResetPasswordViewModel viewModel,
            @Random ResetPassword domainModel
    ) {
        given(mapper.apply(viewModel)).willReturn(domainModel);
        willThrow(new RuntimeException("Invalid or expired token")).given(service).resetPassword(domainModel);

        assertThatThrownBy(() -> sut.resetPassword(viewModel))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid or expired token");
    }
}
