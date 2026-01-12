package com.dndplatform.auth.view.model;

import jakarta.ws.rs.core.Response;

public interface LogoutResource {

    Response logout(String refreshToken, long userId);

}
