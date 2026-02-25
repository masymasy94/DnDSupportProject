package com.dndplatform.user.adapter.inbound.updatepassword;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.user.domain.UserUpdatePasswordService;
import com.dndplatform.user.view.model.UserUpdatePasswordResource;
import com.dndplatform.user.view.model.vm.UserUpdatePasswordViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class UserUpdatePasswordDelegate implements UserUpdatePasswordResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final UserUpdatePasswordService service;

    @Inject
    public UserUpdatePasswordDelegate(UserUpdatePasswordService service) {
        this.service = service;
    }

    @Override
    public void updatePassword(long id, UserUpdatePasswordViewModel vm) {
        log.info(() -> "Updating password for user id: %s".formatted(id));
        service.updatePassword(id, vm.newPassword());
    }
}
