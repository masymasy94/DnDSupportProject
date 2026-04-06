package com.dndplatform.user.domain.impl;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.repository.UserFindByIdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class UserFindByIdServiceImplTest {

    @Mock
    private UserFindByIdRepository userFindByIdRepository;

    private UserFindByIdServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new UserFindByIdServiceImpl(userFindByIdRepository);
    }

    @Test
    void findById_shouldReturnUser_whenUserExists(@Random long userId, @Random User expectedUser) {
        given(userFindByIdRepository.findById(userId)).willReturn(Optional.of(expectedUser));

        Optional<User> result = sut.findById(userId);

        then(result).isPresent().contains(expectedUser);
    }

    @Test
    void findById_shouldReturnEmpty_whenUserNotFound(@Random long userId) {
        given(userFindByIdRepository.findById(userId)).willReturn(Optional.empty());

        Optional<User> result = sut.findById(userId);

        then(result).isEmpty();
    }
}
