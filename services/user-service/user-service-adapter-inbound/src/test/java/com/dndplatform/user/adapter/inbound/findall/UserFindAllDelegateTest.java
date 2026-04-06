package com.dndplatform.user.adapter.inbound.findall;

import com.dndplatform.user.adapter.inbound.register.mapper.UserViewModelMapper;
import com.dndplatform.user.domain.UserFindAllService;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.view.model.vm.PagedUserViewModel;
import com.dndplatform.user.view.model.vm.UserViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith(MockitoExtension.class)
class UserFindAllDelegateTest {

    @Mock
    private UserFindAllService service;

    @Mock
    private UserViewModelMapper mapper;

    private UserFindAllDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new UserFindAllDelegate(service, mapper);
    }

    @org.junit.jupiter.api.Test
    void findAll_shouldReturnPagedUsers() {
        int page = 0;
        int size = 10;
        List<User> users = List.of(
                new User(1L, "user1", "user1@test.com", "hash", "PLAYER", true, null, null),
                new User(2L, "user2", "user2@test.com", "hash", "PLAYER", true, null, null)
        );
        List<UserViewModel> viewModels = List.of(
                new UserViewModel(1L, "user1", "user1@test.com", "PLAYER", true, null),
                new UserViewModel(2L, "user2", "user2@test.com", "PLAYER", true, null)
        );

        given(service.findAll(page, size)).willReturn(users);
        given(service.count()).willReturn(2L);
        given(mapper.apply(users.get(0))).willReturn(viewModels.get(0));
        given(mapper.apply(users.get(1))).willReturn(viewModels.get(1));

        PagedUserViewModel result = sut.findAll(page, size);

        assertThat(result.content()).hasSize(2);
        assertThat(result.page()).isEqualTo(page);
        assertThat(result.size()).isEqualTo(size);
        assertThat(result.totalElements()).isEqualTo(2L);
        assertThat(result.totalPages()).isEqualTo(1);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).findAll(page, size);
        then(mapper).should(inOrder).apply(users.get(0));
        then(mapper).should(inOrder).apply(users.get(1));
        then(service).should(inOrder).count();
    }
}
