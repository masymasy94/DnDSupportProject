package com.dndplatform.auth.adapter.inbound.passwordreset.reset;

import com.dndplatform.auth.view.model.ResetPasswordResource;
import com.dndplatform.auth.view.model.vm.ResetPasswordViewModel;
import com.dndplatform.common.annotations.Delegate;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
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
@Path("/auth/password-resets")
@Tag(name = "Password Reset")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
public class ResetPasswordResourceImpl implements ResetPasswordResource {

    private final ResetPasswordResource delegate;

    @Inject
    public ResetPasswordResourceImpl(@Delegate ResetPasswordResource delegate) {
        this.delegate = delegate;
    }

    @PUT
    @Override
    @Operation(summary = "Reset password using token")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Password reset successfully"),
            @APIResponse(responseCode = "401", description = "Invalid or expired token")
    })
    public Response resetPassword(ResetPasswordViewModel vm) {
        return delegate.resetPassword(vm);
    }
}
