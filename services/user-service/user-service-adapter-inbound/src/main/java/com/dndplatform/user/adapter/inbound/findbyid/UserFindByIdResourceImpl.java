package com.dndplatform.user.adapter.inbound.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.user.view.model.UserFindByIdResource;
import com.dndplatform.user.view.model.vm.UserViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@RequestScoped
@Path("/internal/users/{id}")
@Tag(name = "User", description = "User management operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserFindByIdResourceImpl implements UserFindByIdResource {

    private final UserFindByIdResource delegate;

    @Inject
    public UserFindByIdResourceImpl(@Delegate UserFindByIdResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Override
    @Operation(summary = "Find user by ID", description = "Retrieve user by ID. Requires PLAYER role.")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "User found",
                    content = @Content(schema = @Schema(implementation = UserViewModel.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    public UserViewModel findById(@PathParam("id") long id) {
        return delegate.findById(id);
    }
}
