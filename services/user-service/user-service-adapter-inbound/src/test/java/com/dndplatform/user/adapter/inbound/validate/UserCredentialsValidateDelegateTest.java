package com.dndplatform.user.adapter.inbound.validate;

import com.dndplatform.user.adapter.inbound.register.mapper.UserViewModelMapper;
import com.dndplatform.user.adapter.inbound.validate.mapper.UserCredentialsValidateMapper;
import com.dndplatform.user.domain.UserCredentialsValidateService;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.model.UserCredentialsValidate;
import com.dndplatform.user.view.model.vm.UserCredentialsValidateViewModel;
import com.dndplatform.user.view.model.vm.UserViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith(MockitoExtension.class)
class UserCredentialsValidateDelegateTest {

    @Mock
    private UserCredentialsValidateMapper credentialsMapper;

    @Mock
    private UserViewModelMapper viewModelMapper;

    @Mock
    private UserCredentialsValidateService service;

    private UserCredentialsValidateDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new UserCredentialsValidateDelegate(credentialsMapper, viewModelMapper, service);
    }

    @org.junit.jupiter.api.Test
    void validateUserCredentials_shouldMapViewModelAndCallService() {
        UserCredentialsValidateViewModel viewModel = new UserCredentialsValidateViewModel("testuser", "password123");
        UserCredentialsValidate domainModel = new UserCredentialsValidate("testuser", "password123");
        User validatedUser = new User(1L, "testuser", "test@example.com", "hash", "PLAYER", true, null, null);
        UserViewModel expectedViewModel = new UserViewModel(1L, "testuser", "test@example.com", "PLAYER", true, null);

        given(credentialsMapper.apply(viewModel)).willReturn(domainModel);
        given(service.validateCredentials(domainModel)).willReturn(validatedUser);
        given(viewModelMapper.apply(validatedUser)).willReturn(expectedViewModel);

        UserViewModel result = sut.validateUserCredentials(viewModel);

        assertThat(result).isEqualTo(expectedViewModel);

        var inOrder = inOrder(credentialsMapper, service, viewModelMapper);
        then(credentialsMapper).should(inOrder).apply(viewModel);
        then(service).should(inOrder).validateCredentials(domainModel);
        then(viewModelMapper).should(inOrder).apply(validatedUser);
    }
}
