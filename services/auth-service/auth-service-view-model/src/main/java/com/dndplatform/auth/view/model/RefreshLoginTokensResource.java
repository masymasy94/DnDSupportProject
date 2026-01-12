package com.dndplatform.auth.view.model;

import com.dndplatform.auth.view.model.vm.RefreshTokenViewModel;
import jakarta.ws.rs.core.Response;

public interface RefreshLoginTokensResource {

    Response refreshLoginTokens(RefreshTokenViewModel refreshTokenViewModel);

}
