package com.dndplatform.auth.adapter.outbound.rest.repository;

import com.dndplatform.auth.adapter.outbound.rest.mapper.UserMapper;
import com.dndplatform.auth.domain.model.User;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.client.findbyid.UserFindByIdResourceRestClient;
import com.dndplatform.user.view.model.vm.UserViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class UserFindByIdRepositoryRestTest {

    @Mock
    private UserFindByIdResourceRestClient userFindByIdResourceRestClient;

    @Mock
    private UserMapper userMapper;

    private UserFindByIdRepositoryRest sut;

    @BeforeEach
    void setUp() {
        sut = new UserFindByIdRepositoryRest(userFindByIdResourceRestClient, userMapper);
    }

    @Test
    void shouldReturnMappedUserWhenClientReturnsViewModel(@Random long userId,
                                                          @Random UserViewModel viewModel,
                                                          @Random User expectedUser) {
        given(userFindByIdResourceRestClient.findById(userId)).willReturn(viewModel);
        given(userMapper.apply(viewModel)).willReturn(expectedUser);

        var result = sut.findById(userId);

        assertThat(result).isPresent().contains(expectedUser);
        then(userFindByIdResourceRestClient).should().findById(userId);
        then(userMapper).should().apply(viewModel);
    }

    @Test
    void shouldReturnEmptyWhenClientReturnsNull(@Random long userId) {
        given(userFindByIdResourceRestClient.findById(userId)).willReturn(null);

        var result = sut.findById(userId);

        assertThat(result).isEmpty();
        then(userMapper).shouldHaveNoInteractions();
    }
}
