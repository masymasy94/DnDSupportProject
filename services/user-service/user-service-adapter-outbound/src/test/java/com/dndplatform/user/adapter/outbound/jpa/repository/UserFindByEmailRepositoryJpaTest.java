package com.dndplatform.user.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.adapter.outbound.jpa.mapper.UserMapper;
import com.dndplatform.user.domain.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class UserFindByEmailRepositoryJpaTest {

    @Mock
    private UserMapper mapper;

    @Mock
    private UserPanacheRepository panacheRepository;

    private UserFindByEmailRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new UserFindByEmailRepositoryJpa(mapper, panacheRepository);
    }

    @Test
    void findByEmail_shouldReturnMappedUser_whenEntityFound(@Random User user) {
        String email = "test@example.com";
        UserEntity entity = new UserEntity();
        given(panacheRepository.findByEmail(email)).willReturn(Optional.of(entity));
        given(mapper.apply(entity)).willReturn(user);

        Optional<User> result = sut.findByEmail(email);

        assertThat(result).contains(user);
        then(panacheRepository).should().findByEmail(email);
        then(mapper).should().apply(entity);
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenNotFound() {
        String email = "missing@example.com";
        given(panacheRepository.findByEmail(email)).willReturn(Optional.empty());

        Optional<User> result = sut.findByEmail(email);

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
