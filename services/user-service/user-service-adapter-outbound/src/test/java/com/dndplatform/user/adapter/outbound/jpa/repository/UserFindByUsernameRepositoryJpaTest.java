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
class UserFindByUsernameRepositoryJpaTest {

    @Mock
    private UserMapper mapper;

    @Mock
    private UserPanacheRepository panacheRepository;

    private UserFindByUsernameRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new UserFindByUsernameRepositoryJpa(mapper, panacheRepository);
    }

    @Test
    void findByUsername_shouldReturnMappedUser_whenEntityFound(@Random User user) {
        String username = "gandalf_grey";
        UserEntity entity = new UserEntity();
        given(panacheRepository.findByUsernameOrEmail(username)).willReturn(Optional.of(entity));
        given(mapper.apply(entity)).willReturn(user);

        Optional<User> result = sut.findByUsername(username);

        assertThat(result).contains(user);
        then(panacheRepository).should().findByUsernameOrEmail(username);
        then(mapper).should().apply(entity);
    }

    @Test
    void findByUsername_shouldReturnEmpty_whenNotFound() {
        String username = "unknown_hero";
        given(panacheRepository.findByUsernameOrEmail(username)).willReturn(Optional.empty());

        Optional<User> result = sut.findByUsername(username);

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
