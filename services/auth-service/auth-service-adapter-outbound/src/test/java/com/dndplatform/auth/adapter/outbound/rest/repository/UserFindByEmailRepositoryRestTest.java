package com.dndplatform.auth.adapter.outbound.rest.repository;

import com.dndplatform.auth.adapter.outbound.rest.mapper.UserMapper;
import com.dndplatform.auth.domain.model.User;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.client.findbyemail.UserFindByEmailResourceRestClient;
import com.dndplatform.user.view.model.vm.UserFindByEmailViewModel;
import com.dndplatform.user.view.model.vm.UserViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class UserFindByEmailRepositoryRestTest {

    @Mock
    private UserFindByEmailResourceRestClient userFindByEmailClient;

    @Mock
    private UserMapper userMapper;

    private UserFindByEmailRepositoryRest sut;

    @BeforeEach
    void setUp() {
        sut = new UserFindByEmailRepositoryRest(userFindByEmailClient, userMapper);
    }

    @Test
    void shouldReturnMappedUserWhenClientReturnsViewModel(@Random String email,
                                                          @Random UserViewModel viewModel,
                                                          @Random User expectedUser) {
        given(userFindByEmailClient.findByEmail(any(UserFindByEmailViewModel.class))).willReturn(viewModel);
        given(userMapper.apply(viewModel)).willReturn(expectedUser);

        var result = sut.findByEmail(email);

        assertThat(result).isPresent().contains(expectedUser);
        then(userFindByEmailClient).should().findByEmail(new UserFindByEmailViewModel(email));
        then(userMapper).should().apply(viewModel);
    }

    @Test
    void shouldReturnEmptyWhenClientReturnsNull(@Random String email) {
        given(userFindByEmailClient.findByEmail(any(UserFindByEmailViewModel.class))).willReturn(null);

        var result = sut.findByEmail(email);

        assertThat(result).isEmpty();
        then(userMapper).shouldHaveNoInteractions();
    }

    @Test
    void shouldReturnEmptyWhenClientThrowsNotFoundException(@Random String email) {
        given(userFindByEmailClient.findByEmail(any(UserFindByEmailViewModel.class)))
                .willThrow(new NotFoundException("User not found"));

        var result = sut.findByEmail(email);

        assertThat(result).isEmpty();
        then(userMapper).shouldHaveNoInteractions();
    }
}
