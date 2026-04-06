package com.dndplatform.user.adapter.inbound.updatepassword;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.user.view.model.UserUpdatePasswordResource;
import com.dndplatform.user.view.model.vm.UserUpdatePasswordViewModel;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@RequestScoped
@RunOnVirtualThread
@Path("/users/{id}/password")
@Tag(name = "Internal", description = "Internal service-to-service endpoints")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
public class UserUpdatePasswordResourceImpl implements UserUpdatePasswordResource {

    private final UserUpdatePasswordResource delegate;

    @Inject
    public UserUpdatePasswordResourceImpl(@Delegate UserUpdatePasswordResource delegate) {
        this.delegate = delegate;
    }

    @PUT
    @Override
    @Operation(summary = "Update user password (internal)")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Password updated successfully"),
            @APIResponse(responseCode = "404", description = "User not found")
    })
    public void updatePassword(@PathParam("id") long id, UserUpdatePasswordViewModel vm) {
        delegate.updatePassword(id, vm);
    }
}
