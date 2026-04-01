package com.dndplatform.user.adapter.inbound.findbyemail;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.user.view.model.UserFindByEmailResource;
import com.dndplatform.user.view.model.vm.UserFindByEmailViewModel;
import com.dndplatform.user.view.model.vm.UserViewModel;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@RequestScoped
@RunOnVirtualThread
@Path("/users/email-lookup")
@Tag(name = "Internal", description = "Internal service-to-service endpoints")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
public class UserFindByEmailResourceImpl implements UserFindByEmailResource {

    private final UserFindByEmailResource delegate;

    @Inject
    public UserFindByEmailResourceImpl(@Delegate UserFindByEmailResource delegate) {
        this.delegate = delegate;
    }

    @POST
    @Override
    @Operation(summary = "Find user by email (internal)")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "User found"),
            @APIResponse(responseCode = "404", description = "User not found")
    })
    public UserViewModel findByEmail(UserFindByEmailViewModel vm) {
        return delegate.findByEmail(vm);
    }
}
