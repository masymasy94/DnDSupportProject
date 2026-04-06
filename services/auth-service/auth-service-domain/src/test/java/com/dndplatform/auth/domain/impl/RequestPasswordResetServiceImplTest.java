package com.dndplatform.auth.domain.impl;

import com.dndplatform.auth.domain.model.PasswordResetToken;
import com.dndplatform.auth.domain.model.RequestPasswordReset;
import com.dndplatform.auth.domain.model.User;
import com.dndplatform.auth.domain.repository.PasswordResetEmailSendRepository;
import com.dndplatform.auth.domain.repository.PasswordResetTokenCreateRepository;
import com.dndplatform.auth.domain.repository.UserFindByEmailRepository;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class RequestPasswordResetServiceImplTest {

    @Mock
    private UserFindByEmailRepository userFindByEmailRepository;

    @Mock
    private PasswordResetTokenCreateRepository passwordResetTokenCreateRepository;

    @Mock
    private PasswordResetEmailSendRepository passwordResetEmailSendRepository;

    private RequestPasswordResetServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new RequestPasswordResetServiceImpl(
                userFindByEmailRepository,
                passwordResetTokenCreateRepository,
                passwordResetEmailSendRepository
        );
    }

    @Test
    void shouldSendPasswordResetEmailWhenUserExists(@Random RequestPasswordReset request,
                                                    @Random User user,
                                                    @Random PasswordResetToken createdToken) {
        // Arrange
        given(userFindByEmailRepository.findByEmail(request.email())).willReturn(Optional.of(user));
        given(passwordResetTokenCreateRepository.create(user.id())).willReturn(createdToken);

        // Act
        sut.requestPasswordReset(request);

        // Assert
        var inOrder = inOrder(userFindByEmailRepository, passwordResetTokenCreateRepository, passwordResetEmailSendRepository);
        then(userFindByEmailRepository).should(inOrder).findByEmail(request.email());
        then(passwordResetTokenCreateRepository).should(inOrder).create(user.id());
        then(passwordResetEmailSendRepository).should(inOrder).sendResetEmail(user.email(), createdToken.token());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldReturnSilentlyWhenUserNotFoundToPreventEnumeration(@Random RequestPasswordReset request) {
        // Arrange
        given(userFindByEmailRepository.findByEmail(request.email())).willReturn(Optional.empty());

        // Act
        sut.requestPasswordReset(request);

        // Assert
        then(passwordResetTokenCreateRepository).shouldHaveNoInteractions();
        then(passwordResetEmailSendRepository).shouldHaveNoInteractions();
    }
}
