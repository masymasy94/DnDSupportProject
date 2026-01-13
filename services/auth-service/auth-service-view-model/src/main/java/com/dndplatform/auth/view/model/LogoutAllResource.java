package com.dndplatform.auth.view.model;

import jakarta.ws.rs.core.Response;

public interface LogoutAllResource {

    Response logoutAll(long userId);

}
