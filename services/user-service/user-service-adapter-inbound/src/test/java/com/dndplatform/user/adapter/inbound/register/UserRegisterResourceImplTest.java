package com.dndplatform.user.adapter.inbound.register;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.view.model.UserRegisterResource;
import com.dndplatform.user.view.model.vm.UserRegisterViewModel;
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
class UserRegisterResourceImplTest {

    @Mock
    private UserRegisterResource delegate;

    private UserRegisterResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new UserRegisterResourceImpl(delegate);
    }

    @Test
    void shouldDelegateRegister(@Random UserRegisterViewModel request,
                                @Random UserViewModel expected) {
        given(delegate.register(request)).willReturn(expected);

        var result = sut.register(request);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).register(request);
        inOrder.verifyNoMoreInteractions();
    }
}
