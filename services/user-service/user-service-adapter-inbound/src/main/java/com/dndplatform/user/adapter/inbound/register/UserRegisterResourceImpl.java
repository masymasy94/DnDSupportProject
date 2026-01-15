package com.dndplatform.user.adapter.inbound.register;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.user.view.model.UserRegisterResource;
import com.dndplatform.user.view.model.vm.UserRegisterViewModel;
import com.dndplatform.user.view.model.vm.UserViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@RequestScoped
@Path("/users")
@Tag(name = "User Registration", description = "User registration operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserRegisterResourceImpl implements UserRegisterResource {

    private final UserRegisterResource delegate;

    @Inject
    public UserRegisterResourceImpl(@Delegate UserRegisterResource delegate) {
        this.delegate = delegate;
    }

    @POST
    @Override
    @Operation(summary = "Register a new user", description = "Creates a new user account with the provided details")
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = UserViewModel.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid input data"
            ),
            @APIResponse(
                    responseCode = "409",
                    description = "Username or email already exists"
            )
    })
    public UserViewModel register(UserRegisterViewModel userRegisterViewModel) {
        return delegate.register(userRegisterViewModel);
    }
}
