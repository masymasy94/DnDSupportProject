package com.dndplatform.user.domain.impl;

import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.UnauthorizedException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.model.UserCredentialsValidate;
import com.dndplatform.user.domain.repository.UserFindByUsernameRepository;
import com.dndplatform.user.domain.util.CryptUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class UserCredentialsValidateServiceImplTest {

    @Mock
    private UserFindByUsernameRepository userFindByUsernameRepository;

    private UserCredentialsValidateServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new UserCredentialsValidateServiceImpl(userFindByUsernameRepository);
    }

    @Test
    void validateCredentials_shouldReturnUser_whenCredentialsAreValid(@Random UserCredentialsValidate credentials, @Random User base) {
        User expectedUser = new User(base.id(), base.username(), base.email(), CryptUtil.hashPassword(credentials.password()), base.role(), true, base.createdAt(), base.updatedAt());

        given(userFindByUsernameRepository.findByUsername(credentials.username())).willReturn(Optional.of(expectedUser));

        User result = sut.validateCredentials(credentials);

        then(result).isEqualTo(expectedUser);
    }

    @Test
    void validateCredentials_shouldThrowUnauthorizedException_whenUserNotFound(@Random UserCredentialsValidate credentials) {
        given(userFindByUsernameRepository.findByUsername(credentials.username())).willReturn(Optional.empty());

        thenThrownBy(() -> sut.validateCredentials(credentials))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("Invalid credentials for username");
    }

    @Test
    void validateCredentials_shouldThrowForbiddenException_whenUserIsNotActive(@Random UserCredentialsValidate credentials, @Random User base) {
        User inactiveUser = new User(base.id(), base.username(), base.email(), CryptUtil.hashPassword(credentials.password()), base.role(), false, base.createdAt(), base.updatedAt());

        given(userFindByUsernameRepository.findByUsername(credentials.username())).willReturn(Optional.of(inactiveUser));

        thenThrownBy(() -> sut.validateCredentials(credentials))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("User is not active");
    }
}
