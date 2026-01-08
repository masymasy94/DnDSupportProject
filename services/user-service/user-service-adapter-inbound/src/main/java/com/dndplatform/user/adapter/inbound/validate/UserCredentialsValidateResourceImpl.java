package com.dndplatform.user.adapter.inbound.validate;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.user.view.model.UserCredentialsValidateResource;
import com.dndplatform.user.view.model.vm.UserCredentialsValidateViewModel;
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
@Path("/users/validate-credentials")
@Tag(name = "User Credentials Verification", description = "Internal endpoint for credential verification")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserCredentialsValidateResourceImpl implements UserCredentialsValidateResource {

    private final UserCredentialsValidateResource delegate;

    @Inject
    public UserCredentialsValidateResourceImpl(@Delegate UserCredentialsValidateResource delegate) {
        this.delegate = delegate;
    }

    @POST
    @Override
    @Operation(summary = "Verify user credentials", description = "Internal endpoint to verify user credentials for authentication")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Credentials verified successfully",
                    content = @Content(schema = @Schema(implementation = UserViewModel.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Invalid credentials"
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "User account is not active"
            )
    })
    public UserViewModel validateUserCredentials(UserCredentialsValidateViewModel userCredentialsValidateViewModel) {
        return delegate.validateUserCredentials(userCredentialsValidateViewModel);
    }
}
