package com.dndplatform.auth.adapter.inbound.passwordreset.request;

import com.dndplatform.auth.view.model.RequestPasswordResetResource;
import com.dndplatform.auth.view.model.vm.RequestPasswordResetViewModel;
import com.dndplatform.common.annotations.Delegate;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.PermitAll;
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
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@RequestScoped
@RunOnVirtualThread
@Path("/auth/password-resets")
@Tag(name = "Password Reset")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
public class RequestPasswordResetResourceImpl implements RequestPasswordResetResource {

    private final RequestPasswordResetResource delegate;

    @Inject
    public RequestPasswordResetResourceImpl(@Delegate RequestPasswordResetResource delegate) {
        this.delegate = delegate;
    }

    @POST
    @Override
    @Operation(summary = "Request a password reset email")
    @APIResponse(responseCode = "202", description = "Request accepted")
    public Response requestPasswordReset(RequestPasswordResetViewModel vm) {
        return delegate.requestPasswordReset(vm);
    }
}
