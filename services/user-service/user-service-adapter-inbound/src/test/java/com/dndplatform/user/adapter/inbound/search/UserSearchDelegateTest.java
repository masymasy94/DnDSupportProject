package com.dndplatform.user.adapter.inbound.search;

import com.dndplatform.user.adapter.inbound.register.mapper.UserViewModelMapper;
import com.dndplatform.user.domain.UserSearchService;
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
class UserSearchDelegateTest {

    @Mock
    private UserSearchService service;

    @Mock
    private UserViewModelMapper mapper;

    private UserSearchDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new UserSearchDelegate(service, mapper);
    }

    @org.junit.jupiter.api.Test
    void search_shouldReturnPagedUsers() {
        String query = "test";
        int page = 0;
        int size = 10;
        List<User> users = List.of(
                new User(1L, "testuser1", "user1@test.com", "hash", "PLAYER", true, null, null),
                new User(2L, "testuser2", "user2@test.com", "hash", "PLAYER", true, null, null)
        );
        List<UserViewModel> viewModels = List.of(
                new UserViewModel(1L, "testuser1", "user1@test.com", "PLAYER", true, null),
                new UserViewModel(2L, "testuser2", "user2@test.com", "PLAYER", true, null)
        );

        given(service.search(query, page, size)).willReturn(users);
        given(service.countByQuery(query)).willReturn(2L);
        given(mapper.apply(users.get(0))).willReturn(viewModels.get(0));
        given(mapper.apply(users.get(1))).willReturn(viewModels.get(1));

        PagedUserViewModel result = sut.search(query, page, size);

        assertThat(result.content()).hasSize(2);
        assertThat(result.page()).isEqualTo(page);
        assertThat(result.size()).isEqualTo(size);
        assertThat(result.totalElements()).isEqualTo(2L);
        assertThat(result.totalPages()).isEqualTo(1);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).search(query, page, size);
        then(mapper).should(inOrder).apply(users.get(0));
        then(mapper).should(inOrder).apply(users.get(1));
        then(service).should(inOrder).countByQuery(query);
    }
}
