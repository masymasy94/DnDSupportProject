package com.dndplatform.user.view.model;

import com.dndplatform.user.view.model.vm.UserUpdatePasswordViewModel;
import jakarta.validation.Valid;

public interface UserUpdatePasswordResource {
    void updatePassword(long id, @Valid UserUpdatePasswordViewModel vm);
}
