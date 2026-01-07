package com.dndplatform.auth.view.model;

import com.dndplatform.auth.view.model.vm.LoginResponseViewModel;
import com.dndplatform.auth.view.model.vm.UserLoginViewModel;

public interface UserLoginResource {

    LoginResponseViewModel login(UserLoginViewModel loginCredentialsViewModel);

}
