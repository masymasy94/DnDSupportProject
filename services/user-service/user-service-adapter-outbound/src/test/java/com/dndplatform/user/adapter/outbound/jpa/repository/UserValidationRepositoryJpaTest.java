package com.dndplatform.user.adapter.outbound.jpa.repository;

import com.dndplatform.common.exception.ConflictException;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class UserValidationRepositoryJpaTest {

    @Mock
    private UserPanacheRepository panacheRepository;

    private UserValidationRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new UserValidationRepositoryJpa(panacheRepository);
    }

    @Test
    void existsByUsernameOrEmail_shouldNotThrow_whenNeitherExists() {
        String username = "newuser";
        String email = "newuser@example.com";
        given(panacheRepository.countByUsernameOrEmail(username, email)).willReturn(0L);

        sut.existsByUsernameOrEmail(username, email);

        then(panacheRepository).should().countByUsernameOrEmail(username, email);
    }

    @Test
    void existsByUsernameOrEmail_shouldThrowConflict_whenUsernameOrEmailTaken() {
        String username = "gandalf_grey";
        String email = "gandalf@middleearth.com";
        given(panacheRepository.countByUsernameOrEmail(username, email)).willReturn(1L);

        thenThrownBy(() -> sut.existsByUsernameOrEmail(username, email))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Username or email already exists");
    }
}
