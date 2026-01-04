package com.dndplatform.auth.view.model;

import com.dndplatform.auth.view.model.vm.UserLoginViewModel;

public interface UserLoginResource {

    int login(UserLoginViewModel loginCredentialsViewModel);

}
