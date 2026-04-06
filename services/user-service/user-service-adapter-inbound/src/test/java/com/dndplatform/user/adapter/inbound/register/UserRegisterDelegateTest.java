package com.dndplatform.user.adapter.inbound.register;

import com.dndplatform.user.adapter.inbound.register.mapper.UserRegisterMapper;
import com.dndplatform.user.adapter.inbound.register.mapper.UserViewModelMapper;
import com.dndplatform.user.domain.UserRegisterService;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.model.UserRegister;
import com.dndplatform.user.view.model.vm.UserRegisterViewModel;
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
class UserRegisterDelegateTest {

    @Mock
    private UserRegisterMapper registerMapper;

    @Mock
    private UserViewModelMapper viewModelMapper;

    @Mock
    private UserRegisterService service;

    private UserRegisterDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new UserRegisterDelegate(registerMapper, viewModelMapper, service);
    }

    @org.junit.jupiter.api.Test
    void register_shouldMapViewModelToDomainAndCallService() {
        UserRegisterViewModel viewModel = new UserRegisterViewModel("testuser", "test@example.com", "password123");
        UserRegister domainModel = new UserRegister("testuser", "test@example.com", "password123");
        User createdUser = new User(1L, "testuser", "test@example.com", "hashed", "PLAYER", true, null, null);
        UserViewModel expectedViewModel = new UserViewModel(1L, "testuser", "test@example.com", "PLAYER", true, null);

        given(registerMapper.apply(viewModel)).willReturn(domainModel);
        given(service.register(domainModel)).willReturn(createdUser);
        given(viewModelMapper.apply(createdUser)).willReturn(expectedViewModel);

        UserViewModel result = sut.register(viewModel);

        assertThat(result).isEqualTo(expectedViewModel);

        var inOrder = inOrder(registerMapper, service, viewModelMapper);
        then(registerMapper).should(inOrder).apply(viewModel);
        then(service).should(inOrder).register(domainModel);
        then(viewModelMapper).should(inOrder).apply(createdUser);
    }
}
