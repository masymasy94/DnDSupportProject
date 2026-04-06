package com.dndplatform.user.adapter.inbound.findbyid;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.user.adapter.inbound.register.mapper.UserViewModelMapper;
import com.dndplatform.user.domain.UserFindByIdService;
import com.dndplatform.user.domain.model.User;
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
class UserFindByIdDelegateTest {

    @Mock
    private UserViewModelMapper viewModelMapper;

    @Mock
    private UserFindByIdService service;

    private UserFindByIdDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new UserFindByIdDelegate(viewModelMapper, service);
    }

    @org.junit.jupiter.api.Test
    void findById_shouldReturnUserViewModel_whenUserExists() {
        long userId = 1L;
        User user = new User(userId, "testuser", "test@example.com", "hash", "PLAYER", true, null, null);
        UserViewModel expectedViewModel = new UserViewModel(userId, "testuser", "test@example.com", "PLAYER", true, null);

        given(service.findById(userId)).willReturn(Optional.of(user));
        given(viewModelMapper.apply(user)).willReturn(expectedViewModel);

        UserViewModel result = sut.findById(userId);

        assertThat(result).isEqualTo(expectedViewModel);

        var inOrder = inOrder(service, viewModelMapper);
        then(service).should(inOrder).findById(userId);
        then(viewModelMapper).should(inOrder).apply(user);
    }

    @org.junit.jupiter.api.Test
    void findById_shouldThrowNotFoundException_whenUserNotFound() {
        long userId = 999L;

        given(service.findById(userId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.findById(userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User not found with id: " + userId);
    }
}
