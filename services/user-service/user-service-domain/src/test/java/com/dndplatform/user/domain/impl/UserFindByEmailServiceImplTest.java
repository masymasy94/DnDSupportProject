package com.dndplatform.user.domain.impl;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.repository.UserFindByEmailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class UserFindByEmailServiceImplTest {

    @Mock
    private UserFindByEmailRepository userFindByEmailRepository;

    private UserFindByEmailServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new UserFindByEmailServiceImpl(userFindByEmailRepository);
    }

    @Test
    void findByEmail_shouldReturnUser_whenUserExists(@Random String email, @Random User expectedUser) {
        given(userFindByEmailRepository.findByEmail(email)).willReturn(Optional.of(expectedUser));

        Optional<User> result = sut.findByEmail(email);

        then(result).isPresent().contains(expectedUser);
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenUserNotFound(@Random String email) {
        given(userFindByEmailRepository.findByEmail(email)).willReturn(Optional.empty());

        Optional<User> result = sut.findByEmail(email);

        then(result).isEmpty();
    }
}
