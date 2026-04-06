package com.dndplatform.user.domain.impl;

import com.dndplatform.common.exception.ConflictException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.domain.event.EmailSendRepository;
import com.dndplatform.user.domain.event.UserRegisteredEvent;
import com.dndplatform.user.domain.event.UserRegisteredEventMapper;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.model.UserRegister;
import com.dndplatform.user.domain.repository.UserCreateRepository;
import com.dndplatform.user.domain.repository.UserValidationRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class UserRegisterServiceImplTest {

    private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2026, 1, 1, 12, 0);
    private final Clock clock = Clock.fixed(FIXED_TIME.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    @Mock
    private UserValidationRepository userValidationRepository;

    @Mock
    private UserCreateRepository userCreateRepository;

    @Mock
    private EmailSendRepository emailSendRepository;

    @Mock
    private UserRegisteredEventMapper userRegisteredEventMapper;

    private UserRegisterServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new UserRegisterServiceImpl(
                userValidationRepository,
                userCreateRepository,
                emailSendRepository,
                userRegisteredEventMapper,
                clock
        );
    }

    @Test
    void register_shouldCreateUserAndSendEmail(@Random UserRegister userRegister, @Random User createdUser, @Random UserRegisteredEvent event) {
        given(userCreateRepository.create(any())).willReturn(createdUser);
        given(userRegisteredEventMapper.apply(createdUser)).willReturn(event);

        User result = sut.register(userRegister);

        assertThat(result).isEqualTo(createdUser);

        var inOrder = inOrder(userValidationRepository, userCreateRepository, emailSendRepository);
        then(userValidationRepository).should(inOrder).existsByUsernameOrEmail(userRegister.username(), userRegister.email());
        then(userCreateRepository).should(inOrder).create(any());
        then(emailSendRepository).should(inOrder).sendEmail(event);
    }

    @Test
    void register_shouldThrowConflictException_whenUsernameOrEmailExists(@Random UserRegister userRegister) {
        willThrow(new ConflictException("Username or email already exists"))
                .given(userValidationRepository).existsByUsernameOrEmail(userRegister.username(), userRegister.email());

        thenThrownBy(() -> sut.register(userRegister))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Username or email already exists");

        then(userCreateRepository).shouldHaveNoInteractions();
        then(emailSendRepository).shouldHaveNoInteractions();
    }
}
