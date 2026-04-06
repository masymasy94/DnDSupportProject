package com.dndplatform.auth.adapter.outbound.rest.repository;

import com.dndplatform.auth.adapter.outbound.rest.mapper.UserCredentialsValidateViewModelMapper;
import com.dndplatform.auth.domain.model.User;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.client.validate.UserCertificationsValidateResourceRestClient;
import com.dndplatform.user.view.model.vm.UserCredentialsValidateViewModel;
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
class UserCredentialsValidateRepositoryRestTest {

    @Mock
    private UserCertificationsValidateResourceRestClient userServiceClient;

    @Mock
    private UserCredentialsValidateViewModelMapper mapper;

    private UserCredentialsValidateRepositoryRest sut;

    @BeforeEach
    void setUp() {
        sut = new UserCredentialsValidateRepositoryRest(userServiceClient, mapper);
    }

    @Test
    void shouldValidateCredentialsAndReturnMappedUser(@Random String username,
                                                      @Random String password,
                                                      @Random UserViewModel viewModel,
                                                      @Random User expectedUser) {
        given(userServiceClient.validateUserCredentials(any(UserCredentialsValidateViewModel.class))).willReturn(viewModel);
        given(mapper.apply(viewModel)).willReturn(expectedUser);

        var result = sut.validateCredentials(username, password);

        assertThat(result).isEqualTo(expectedUser);
        then(userServiceClient).should().validateUserCredentials(new UserCredentialsValidateViewModel(username, password));
        then(mapper).should().apply(viewModel);
    }
}
