package com.dndplatform.user.domain.impl;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.repository.UserFindAllRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class UserFindAllServiceImplTest {

    @Mock
    private UserFindAllRepository repository;

    private UserFindAllServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new UserFindAllServiceImpl(repository);
    }

    @Test
    void findAll_shouldReturnUsers(@Random int page, @Random int size, @Random User user1, @Random User user2) {
        List<User> expectedUsers = List.of(user1, user2);

        given(repository.findAll(page, size)).willReturn(expectedUsers);

        List<User> result = sut.findAll(page, size);

        then(result).isEqualTo(expectedUsers);
    }

    @Test
    void count_shouldReturnTotalCount(@Random long expectedCount) {
        given(repository.count()).willReturn(expectedCount);

        long result = sut.count();

        then(result).isEqualTo(expectedCount);
    }
}
