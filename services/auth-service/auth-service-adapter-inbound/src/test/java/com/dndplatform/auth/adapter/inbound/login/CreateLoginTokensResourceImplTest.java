package com.dndplatform.auth.adapter.inbound.login;

import com.dndplatform.auth.view.model.CreateLoginTokensResource;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CreateLoginTokensResourceImplTest {

    @Mock
    private CreateLoginTokensResource delegate;

    @Mock
    private Response response;

    private CreateLoginTokensResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CreateLoginTokensResourceImpl(delegate);
    }

    @Test
    void shouldDelegateCreateLoginTokens(@Random CreateLoginTokensViewModel request) {
        given(delegate.createLoginTokens(request)).willReturn(response);

        var result = sut.createLoginTokens(request);

        assertThat(result).isEqualTo(response);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).createLoginTokens(request);
        inOrder.verifyNoMoreInteractions();
    }
}
