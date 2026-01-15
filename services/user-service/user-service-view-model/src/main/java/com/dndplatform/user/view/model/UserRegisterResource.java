package com.dndplatform.user.view.model;

import com.dndplatform.user.view.model.vm.UserRegisterViewModel;
import com.dndplatform.user.view.model.vm.UserViewModel;
import jakarta.validation.Valid;

public interface UserRegisterResource {

    UserViewModel register(@Valid UserRegisterViewModel userRegisterViewModel);
}
