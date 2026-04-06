package com.dndplatform.auth.domain.impl;

import com.dndplatform.auth.domain.model.OtpLoginToken;
import com.dndplatform.auth.domain.model.RequestOtpLogin;
import com.dndplatform.auth.domain.model.User;
import com.dndplatform.auth.domain.repository.OtpLoginEmailSendRepository;
import com.dndplatform.auth.domain.repository.OtpLoginTokenCreateRepository;
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
class RequestOtpLoginServiceImplTest {

    @Mock
    private UserFindByEmailRepository userFindByEmailRepository;

    @Mock
    private OtpLoginTokenCreateRepository otpLoginTokenCreateRepository;

    @Mock
    private OtpLoginEmailSendRepository otpLoginEmailSendRepository;

    private RequestOtpLoginServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new RequestOtpLoginServiceImpl(
                userFindByEmailRepository,
                otpLoginTokenCreateRepository,
                otpLoginEmailSendRepository
        );
    }

    @Test
    void shouldSendOtpLoginEmailWhenUserExists(@Random RequestOtpLogin request,
                                               @Random User user,
                                               @Random OtpLoginToken createdToken) {
        // Arrange
        given(userFindByEmailRepository.findByEmail(request.email())).willReturn(Optional.of(user));
        given(otpLoginTokenCreateRepository.create(user.id())).willReturn(createdToken);

        // Act
        sut.requestOtpLogin(request);

        // Assert
        var inOrder = inOrder(userFindByEmailRepository, otpLoginTokenCreateRepository, otpLoginEmailSendRepository);
        then(userFindByEmailRepository).should(inOrder).findByEmail(request.email());
        then(otpLoginTokenCreateRepository).should(inOrder).create(user.id());
        then(otpLoginEmailSendRepository).should(inOrder).sendOtpLoginEmail(user.email(), createdToken.token());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldReturnSilentlyWhenUserNotFoundToPreventEnumeration(@Random RequestOtpLogin request) {
        // Arrange
        given(userFindByEmailRepository.findByEmail(request.email())).willReturn(Optional.empty());

        // Act
        sut.requestOtpLogin(request);

        // Assert
        then(otpLoginTokenCreateRepository).shouldHaveNoInteractions();
        then(otpLoginEmailSendRepository).shouldHaveNoInteractions();
    }
}
