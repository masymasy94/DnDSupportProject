package com.dndplatform.user.view.model;

import com.dndplatform.user.view.model.vm.UserViewModel;
import com.dndplatform.user.view.model.vm.UserCredentialsValidateViewModel;
import jakarta.validation.Valid;

public interface UserCredentialsValidateResource {

    UserViewModel validateUserCredentials(@Valid UserCredentialsValidateViewModel userCredentialsValidateViewModel);
}
