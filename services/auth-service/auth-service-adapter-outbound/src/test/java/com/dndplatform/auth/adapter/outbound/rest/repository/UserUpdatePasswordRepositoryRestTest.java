package com.dndplatform.auth.adapter.outbound.rest.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.client.updatepassword.UserUpdatePasswordResourceRestClient;
import com.dndplatform.user.view.model.vm.UserUpdatePasswordViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class UserUpdatePasswordRepositoryRestTest {

    @Mock
    private UserUpdatePasswordResourceRestClient userUpdatePasswordClient;

    private UserUpdatePasswordRepositoryRest sut;

    @BeforeEach
    void setUp() {
        sut = new UserUpdatePasswordRepositoryRest(userUpdatePasswordClient);
    }

    @Test
    void shouldDelegateUpdatePasswordToRestClient(@Random long userId, @Random String newPassword) {
        sut.updatePassword(userId, newPassword);

        then(userUpdatePasswordClient).should().updatePassword(userId, new UserUpdatePasswordViewModel(newPassword));
    }
}
