package com.dndplatform.auth.adapter.inbound.login;

import com.dndplatform.auth.view.model.CreateLoginTokensResource;
import com.dndplatform.auth.view.model.vm.CreateLoginTokensViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@RequestScoped
@Path("/auth/login-tokens")
@Tag(name = "Login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CreateLoginTokensResourceImpl implements CreateLoginTokensResource {

    private final CreateLoginTokensResource delegate;

    @Inject
    public CreateLoginTokensResourceImpl(@Delegate CreateLoginTokensResource delegate) {
        this.delegate = delegate;
    }

    @POST
    @Override
    public Response createLoginTokens(CreateLoginTokensViewModel createLoginTokensViewModel) {
        return delegate.createLoginTokens(createLoginTokensViewModel);
    }

}
