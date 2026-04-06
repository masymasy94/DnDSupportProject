package com.dndplatform.user.domain.impl;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.domain.repository.UserUpdatePasswordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class UserUpdatePasswordServiceImplTest {

    @Mock
    private UserUpdatePasswordRepository userUpdatePasswordRepository;

    private UserUpdatePasswordServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new UserUpdatePasswordServiceImpl(userUpdatePasswordRepository);
    }

    @Test
    void updatePassword_shouldCallRepository(@Random long userId, @Random String newPassword) {
        sut.updatePassword(userId, newPassword);

        var inOrder = inOrder(userUpdatePasswordRepository);
        then(userUpdatePasswordRepository).should(inOrder).updatePassword(eq(userId), anyString());
    }
}
