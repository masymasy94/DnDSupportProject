package com.dndplatform.user.domain.impl;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.repository.UserSearchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class UserSearchServiceImplTest {

    @Mock
    private UserSearchRepository repository;

    private UserSearchServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new UserSearchServiceImpl(repository);
    }

    @Test
    void search_shouldReturnUsers(@Random String query, @Random int page, @Random int size, @Random User user1, @Random User user2) {
        List<User> expectedUsers = List.of(user1, user2);

        given(repository.search(query, page, size)).willReturn(expectedUsers);

        List<User> result = sut.search(query, page, size);

        then(result).isEqualTo(expectedUsers);
    }

    @Test
    void countByQuery_shouldReturnCount(@Random String query, @Random long expectedCount) {
        given(repository.countByQuery(query)).willReturn(expectedCount);

        long result = sut.countByQuery(query);

        then(result).isEqualTo(expectedCount);
    }
}
