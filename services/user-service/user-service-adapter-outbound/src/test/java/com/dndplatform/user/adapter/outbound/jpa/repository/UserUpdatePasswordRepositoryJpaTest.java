package com.dndplatform.user.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class UserUpdatePasswordRepositoryJpaTest {

    @Mock
    private UserPanacheRepository panacheRepository;

    private UserUpdatePasswordRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new UserUpdatePasswordRepositoryJpa(panacheRepository);
    }

    @Test
    void updatePassword_shouldDelegateToRepository() {
        long userId = 42L;
        String passwordHash = "newHashedPassword";
        given(panacheRepository.updatePassword(eq(userId), eq(passwordHash), any(LocalDateTime.class))).willReturn(1);

        sut.updatePassword(userId, passwordHash);

        then(panacheRepository).should().updatePassword(eq(userId), eq(passwordHash), any(LocalDateTime.class));
    }
}
