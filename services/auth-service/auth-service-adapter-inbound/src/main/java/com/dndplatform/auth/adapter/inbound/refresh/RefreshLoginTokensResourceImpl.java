package com.dndplatform.auth.adapter.inbound.refresh;

import com.dndplatform.auth.view.model.RefreshLoginTokensResource;
import com.dndplatform.auth.view.model.vm.RefreshTokenViewModel;
import com.dndplatform.common.annotations.Delegate;
import io.smallrye.common.annotation.RunOnVirtualThread;
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
@RunOnVirtualThread
@Path("/auth/login-tokens/refreshed")
@Tag(name = "Refresh login token")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RefreshLoginTokensResourceImpl implements RefreshLoginTokensResource {

    private final RefreshLoginTokensResource delegate;

    @Inject
    public RefreshLoginTokensResourceImpl(@Delegate RefreshLoginTokensResource delegate) {
        this.delegate = delegate;
    }

    @POST
    @Override
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Token refreshed successfully"),
            @APIResponse(responseCode = "401", description = "Invalid or expired refresh token"),
            @APIResponse(responseCode = "403", description = "Token revoked or user inactive")
    })
    @Operation(summary = "Refresh access token", description = "Uses refresh gives token to generate a new access token")
    public Response refreshLoginTokens(RefreshTokenViewModel refreshTokenViewModel) {
        return delegate.refreshLoginTokens(refreshTokenViewModel);
    }

}
