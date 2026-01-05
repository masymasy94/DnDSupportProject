package com.dndplatform.auth.adapter.inbound.login;

import com.dndplatform.auth.view.model.UserLoginResource;
import com.dndplatform.auth.view.model.vm.UserLoginViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@RequestScoped
@Path("/auth/login")
@Tag(name = "Login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
//@SecurityRequirement(name = "bearerAuth")
//@SecurityScheme(
//        description = "token authorization",
//        securitySchemeName = "token",
//        type = APIKEY,
//        apiKeyName = "x-service-token",
//        in = HEADER
//)
public class UserLoginResourceImpl implements UserLoginResource {

    private final UserLoginResource delegate;

    @Inject
    public UserLoginResourceImpl(@Delegate UserLoginResource delegate) {
        this.delegate = delegate;
    }


    @POST
    @Override
    //@SecurityRequirement(name = "token")
    //@RolesAllowed({"PLAYER", "DM", "ADMIN"})
    public String login(UserLoginViewModel userLoginViewModel) {

        // todo - add validation
        return delegate.login(userLoginViewModel);
    }

}
