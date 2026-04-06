package com.dndplatform.user.adapter.inbound.findbyemail;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.user.adapter.inbound.register.mapper.UserViewModelMapper;
import com.dndplatform.user.domain.UserFindByEmailService;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.view.model.vm.UserFindByEmailViewModel;
import com.dndplatform.user.view.model.vm.UserViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith(MockitoExtension.class)
class UserFindByEmailDelegateTest {

    @Mock
    private UserViewModelMapper viewModelMapper;

    @Mock
    private UserFindByEmailService service;

    private UserFindByEmailDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new UserFindByEmailDelegate(viewModelMapper, service);
    }

    @org.junit.jupiter.api.Test
    void findByEmail_shouldReturnUserViewModel_whenUserExists() {
        String email = "test@example.com";
        UserFindByEmailViewModel viewModel = new UserFindByEmailViewModel(email);
        User user = new User(1L, "testuser", email, "hash", "PLAYER", true, null, null);
        UserViewModel expectedViewModel = new UserViewModel(1L, "testuser", email, "PLAYER", true, null);

        given(service.findByEmail(email)).willReturn(Optional.of(user));
        given(viewModelMapper.apply(user)).willReturn(expectedViewModel);

        UserViewModel result = sut.findByEmail(viewModel);

        assertThat(result).isEqualTo(expectedViewModel);

        var inOrder = inOrder(service, viewModelMapper);
        then(service).should(inOrder).findByEmail(email);
        then(viewModelMapper).should(inOrder).apply(user);
    }

    @org.junit.jupiter.api.Test
    void findByEmail_shouldThrowNotFoundException_whenUserNotFound() {
        String email = "notfound@example.com";
        UserFindByEmailViewModel viewModel = new UserFindByEmailViewModel(email);

        given(service.findByEmail(email)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.findByEmail(viewModel))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User not found with email: " + email);
    }
}
