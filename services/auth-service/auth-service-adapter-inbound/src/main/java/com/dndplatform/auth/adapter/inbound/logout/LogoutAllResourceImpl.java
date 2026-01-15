package com.dndplatform.auth.adapter.inbound.logout;

import com.dndplatform.auth.view.model.LogoutAllResource;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@RequestScoped
@Path("/auth/login-tokens")
@Tag(name = "Logout All")
@Produces(MediaType.APPLICATION_JSON)
public class LogoutAllResourceImpl implements LogoutAllResource {

    private final LogoutAllResource delegate;

    @Inject
    public LogoutAllResourceImpl(@Delegate LogoutAllResource delegate) {
        this.delegate = delegate;
    }

    @DELETE
    @Override
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Logout all sessions successful"),
            @APIResponse(responseCode = "401", description = "Unauthorized")
    })
    @Operation(summary = "Logout all user sessions", description = "Revokes all refresh tokens for the user")
    public Response logoutAll(
            @Parameter(description = "User ID", required = true)
            @QueryParam("userId") long userId) {
        return delegate.logoutAll(userId);
    }

}
