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
class UserSearchRepositoryJpaTest {

    @Mock
    private UserMapper mapper;

    @Mock
    private UserPanacheRepository panacheRepository;

    private UserSearchRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new UserSearchRepositoryJpa(mapper, panacheRepository);
    }

    @Test
    void search_shouldReturnMappedResults() {
        String query = "gandalf";
        String pattern = "%gandalf%";
        int page = 0;
        int size = 10;
        UserEntity entity = new UserEntity();
        User user = new User(1L, "gandalf_grey", "gandalf@example.com", "hash", "PLAYER", true, null, null);

        given(panacheRepository.searchPaged(pattern, page, size)).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(user);

        List<User> result = sut.search(query, page, size);

        assertThat(result).containsExactly(user);
        then(panacheRepository).should().searchPaged(pattern, page, size);
    }

    @Test
    void countByQuery_shouldReturnCountFromRepository() {
        String query = "Gand";
        String pattern = "%gand%";
        given(panacheRepository.countByQuery(pattern)).willReturn(3L);

        long result = sut.countByQuery(query);

        assertThat(result).isEqualTo(3L);
        then(panacheRepository).should().countByQuery(pattern);
    }
}
