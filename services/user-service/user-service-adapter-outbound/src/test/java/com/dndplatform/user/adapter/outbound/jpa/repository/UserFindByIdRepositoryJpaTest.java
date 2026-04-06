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
class UserFindByIdRepositoryJpaTest {

    @Mock
    private UserMapper mapper;

    @Mock
    private UserPanacheRepository panacheRepository;

    private UserFindByIdRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new UserFindByIdRepositoryJpa(mapper, panacheRepository);
    }

    @Test
    void findById_shouldReturnMappedUser_whenEntityFound(@Random User user) {
        long id = 7L;
        UserEntity entity = new UserEntity();
        given(panacheRepository.findByIdOptional(id)).willReturn(Optional.of(entity));
        given(mapper.apply(entity)).willReturn(user);

        Optional<User> result = sut.findById(id);

        assertThat(result).contains(user);
        then(panacheRepository).should().findByIdOptional(id);
        then(mapper).should().apply(entity);
    }

    @Test
    void findById_shouldReturnEmpty_whenNotFound() {
        long id = 99L;
        given(panacheRepository.findByIdOptional(id)).willReturn(Optional.empty());

        Optional<User> result = sut.findById(id);

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
