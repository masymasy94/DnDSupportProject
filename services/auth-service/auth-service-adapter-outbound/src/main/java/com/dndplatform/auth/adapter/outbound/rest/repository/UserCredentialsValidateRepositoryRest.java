package com.dndplatform.auth.adapter.outbound.rest.repository;

import com.dndplatform.auth.adapter.outbound.rest.mapper.UserCredentialsValidateViewModelMapper;
import com.dndplatform.auth.domain.model.User;
import com.dndplatform.auth.domain.model.UserLogin;
import com.dndplatform.auth.domain.repository.UserCredentialsValidateRepository;
import com.dndplatform.user.client.validate.UserCertificationsValidateResourceRestClient;
import com.dndplatform.user.view.model.vm.UserCredentialsValidateViewModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class UserCredentialsValidateRepositoryRest implements UserCredentialsValidateRepository {

    private final UserCertificationsValidateResourceRestClient userServiceClient;
    private final UserCredentialsValidateViewModelMapper mapper;

    @Inject
    public UserCredentialsValidateRepositoryRest(@RestClient UserCertificationsValidateResourceRestClient userServiceClient,
                                                 UserCredentialsValidateViewModelMapper mapper) {
        this.userServiceClient = userServiceClient;
        this.mapper = mapper;
    }

    @Override
    public User validateCredentials(UserLogin userLogin) {

        //todo mapper
        var request = new UserCredentialsValidateViewModel(userLogin.username(), userLogin.password());
        var response = userServiceClient.validateUserCredentials(request);
        return mapper.apply(response);

    }
}
