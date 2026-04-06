package com.dndplatform.user.adapter.inbound.updatepassword;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.view.model.UserUpdatePasswordResource;
import com.dndplatform.user.view.model.vm.UserUpdatePasswordViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class UserUpdatePasswordResourceImplTest {

    @Mock
    private UserUpdatePasswordResource delegate;

    private UserUpdatePasswordResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new UserUpdatePasswordResourceImpl(delegate);
    }

    @Test
    void shouldDelegateUpdatePassword(@Random UserUpdatePasswordViewModel request) {
        long id = 1L;

        sut.updatePassword(id, request);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).updatePassword(id, request);
        inOrder.verifyNoMoreInteractions();
    }
}
