package com.dndplatform.auth.view.model;

import com.dndplatform.auth.view.model.vm.UserLoginViewModel;
import jakarta.ws.rs.core.Response;

public interface UserLoginResource {

    Response login(UserLoginViewModel loginCredentialsViewModel);

}
