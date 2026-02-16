package com.dndplatform.auth.adapter.inbound.logout;

import com.dndplatform.auth.view.model.LogoutResource;
import com.dndplatform.common.annotations.Delegate;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@RequestScoped
@RunOnVirtualThread
@Path("/auth/login-tokens/{refreshToken}")
@Tag(name = "Logout")
@Produces(MediaType.APPLICATION_JSON)
public class LogoutResourceImpl implements LogoutResource {

    private final LogoutResource delegate;

    @Inject
    public LogoutResourceImpl(@Delegate LogoutResource delegate) {
        this.delegate = delegate;
    }

    @DELETE
    @Override
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Logout successful"),
            @APIResponse(responseCode = "401", description = "Invalid or expired refresh token")
    })
    @Operation(summary = "Logout user", description = "Revokes the refresh token to log out the user")
    public Response logout(
            @Parameter(description = "Refresh token to be revoked", required = true)
            @PathParam("refreshToken") String refreshToken,
            @Parameter(description = "User ID", required = true)
            @QueryParam("userId") long userId) {
        return delegate.logout(refreshToken, userId);
    }

}
