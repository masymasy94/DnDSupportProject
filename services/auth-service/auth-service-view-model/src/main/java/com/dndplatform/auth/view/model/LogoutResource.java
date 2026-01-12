package com.dndplatform.auth.view.model;

import com.dndplatform.auth.view.model.vm.LogoutViewModel;
import jakarta.ws.rs.core.Response;

public interface LogoutResource {

    Response logout(LogoutViewModel logoutViewModel);

}
