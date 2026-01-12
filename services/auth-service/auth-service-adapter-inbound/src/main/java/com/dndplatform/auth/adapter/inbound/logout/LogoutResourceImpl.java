package com.dndplatform.auth.adapter.inbound.logout;

import com.dndplatform.auth.view.model.LogoutResource;
import com.dndplatform.auth.view.model.vm.LogoutViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@RequestScoped
@Path("/auth/logout")
@Tag(name = "Logout")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LogoutResourceImpl implements LogoutResource {

    private final LogoutResource delegate;

    @Inject
    public LogoutResourceImpl(@Delegate LogoutResource delegate) {
        this.delegate = delegate;
    }

    @POST
    @Override
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Logout successful"),
            @APIResponse(responseCode = "401", description = "Invalid or expired refresh token")
    })
    @Operation(summary = "Logout user", description = "Revokes the refresh token to log out the user")
    public Response logout(LogoutViewModel logoutViewModel) {
        return delegate.logout(logoutViewModel);
    }

}
