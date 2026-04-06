package com.dndplatform.user.adapter.inbound.validate;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.view.model.UserCredentialsValidateResource;
import com.dndplatform.user.view.model.vm.UserCredentialsValidateViewModel;
import com.dndplatform.user.view.model.vm.UserViewModel;
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
class UserCredentialsValidateResourceImplTest {

    @Mock
    private UserCredentialsValidateResource delegate;

    private UserCredentialsValidateResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new UserCredentialsValidateResourceImpl(delegate);
    }

    @Test
    void shouldDelegateValidateUserCredentials(@Random UserCredentialsValidateViewModel request,
                                               @Random UserViewModel expected) {
        given(delegate.validateUserCredentials(request)).willReturn(expected);

        var result = sut.validateUserCredentials(request);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).validateUserCredentials(request);
        inOrder.verifyNoMoreInteractions();
    }
}
