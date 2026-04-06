package com.dndplatform.user.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.adapter.outbound.jpa.mapper.UserMapper;
import com.dndplatform.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class UserFindAllRepositoryJpaTest {

    @Mock
    private UserMapper mapper;

    @Mock
    private UserPanacheRepository panacheRepository;

    private UserFindAllRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new UserFindAllRepositoryJpa(mapper, panacheRepository);
    }

    @Test
    void findAll_shouldReturnMappedUsers() {
        int page = 0;
        int size = 10;
        UserEntity entity1 = new UserEntity();
        UserEntity entity2 = new UserEntity();
        List<UserEntity> entities = List.of(entity1, entity2);
        User user1 = new User(1L, "alice", "alice@example.com", "hash", "PLAYER", true, null, null);
        User user2 = new User(2L, "bob", "bob@example.com", "hash", "PLAYER", true, null, null);

        given(panacheRepository.findAllPaged(page, size)).willReturn(entities);
        given(mapper.apply(entity1)).willReturn(user1);
        given(mapper.apply(entity2)).willReturn(user2);

        List<User> result = sut.findAll(page, size);

        assertThat(result).containsExactly(user1, user2);
        then(panacheRepository).should().findAllPaged(page, size);
    }

    @Test
    void count_shouldDelegateToRepository() {
        given(panacheRepository.count()).willReturn(42L);

        long result = sut.count();

        assertThat(result).isEqualTo(42L);
        then(panacheRepository).should().count();
    }
}
