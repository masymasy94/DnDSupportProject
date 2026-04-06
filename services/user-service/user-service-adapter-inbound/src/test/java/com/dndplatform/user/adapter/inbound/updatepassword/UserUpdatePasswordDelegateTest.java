package com.dndplatform.user.adapter.inbound.updatepassword;

import com.dndplatform.user.domain.UserUpdatePasswordService;
import com.dndplatform.user.view.model.vm.UserUpdatePasswordViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith(MockitoExtension.class)
class UserUpdatePasswordDelegateTest {

    @Mock
    private UserUpdatePasswordService service;

    private UserUpdatePasswordDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new UserUpdatePasswordDelegate(service);
    }

    @org.junit.jupiter.api.Test
    void updatePassword_shouldCallService() {
        long userId = 1L;
        UserUpdatePasswordViewModel viewModel = new UserUpdatePasswordViewModel("newPassword123");

        sut.updatePassword(userId, viewModel);

        var inOrder = inOrder(service);
        then(service).should(inOrder).updatePassword(userId, viewModel.newPassword());
    }
}
