package com.dndplatform.auth.view.model;

import com.dndplatform.auth.view.model.vm.CreateLoginTokensViewModel;
import jakarta.ws.rs.core.Response;

public interface CreateLoginTokensResource {

    Response createLoginTokens(CreateLoginTokensViewModel loginCredentialsViewModel);

}
