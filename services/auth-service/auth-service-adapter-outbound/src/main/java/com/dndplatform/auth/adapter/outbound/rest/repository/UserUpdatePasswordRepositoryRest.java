package com.dndplatform.auth.adapter.outbound.rest.repository;

import com.dndplatform.auth.domain.repository.UserUpdatePasswordRepository;
import com.dndplatform.user.client.updatepassword.UserUpdatePasswordResourceRestClient;
import com.dndplatform.user.view.model.vm.UserUpdatePasswordViewModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.logging.Logger;

@ApplicationScoped
public class UserUpdatePasswordRepositoryRest implements UserUpdatePasswordRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final UserUpdatePasswordResourceRestClient userUpdatePasswordClient;

    @Inject
    public UserUpdatePasswordRepositoryRest(@RestClient UserUpdatePasswordResourceRestClient userUpdatePasswordClient) {
        this.userUpdatePasswordClient = userUpdatePasswordClient;
    }

    @Override
    public void updatePassword(long userId, String newPassword) {
        log.info(() -> "Calling user-service to update password for user id: %s".formatted(userId));
        var vm = new UserUpdatePasswordViewModel(newPassword);
        userUpdatePasswordClient.updatePassword(userId, vm);
    }
}
