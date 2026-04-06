package com.dndplatform.user.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.adapter.outbound.jpa.mapper.UserEntityMapper;
import com.dndplatform.user.adapter.outbound.jpa.mapper.UserMapper;
import com.dndplatform.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class UserCreateRepositoryJpaTest {

    @Mock
    private UserEntityMapper entityMapper;

    @Mock
    private UserMapper domainMapper;

    @Mock
    private UserPanacheRepository panacheRepository;

    private UserCreateRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new UserCreateRepositoryJpa(entityMapper, domainMapper, panacheRepository);
    }

    @Test
    void create_shouldPersistEntityAndReturnDomainUser(@Random User user, @Random User savedUser) {
        UserEntity entity = new UserEntity();
        given(entityMapper.apply(user)).willReturn(entity);
        willDoNothing().given(panacheRepository).persist(any(UserEntity.class));
        given(domainMapper.apply(entity)).willReturn(savedUser);

        User result = sut.create(user);

        assertThat(result).isEqualTo(savedUser);
        then(entityMapper).should().apply(user);
        then(panacheRepository).should().persist(entity);
        then(domainMapper).should().apply(entity);
    }
}
