package com.dndplatform.user.adapter.inbound.search;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.user.view.model.UserSearchResource;
import com.dndplatform.user.view.model.vm.PagedUserViewModel;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import static org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType.HTTP;

@RequestScoped
@RunOnVirtualThread
@Path("/users/search")
@Tag(name = "User", description = "User management operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class UserSearchResourceImpl implements UserSearchResource {

    private final UserSearchResource delegate;

    @Inject
    public UserSearchResourceImpl(@Delegate UserSearchResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Override
    @Operation(summary = "Search users", description = "Search users by username or email")
    @APIResponse(
            responseCode = "200",
            description = "Search results retrieved successfully",
            content = @Content(schema = @Schema(implementation = PagedUserViewModel.class))
    )
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public PagedUserViewModel search(
            @Parameter(description = "Search query (matches username or email)", required = true, example = "gandalf")
            @QueryParam("query") String query,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @QueryParam("page") @DefaultValue("0") int page,
            @Parameter(description = "Page size", example = "20")
            @QueryParam("size") @DefaultValue("20") int size) {
        return delegate.search(query, page, size);
    }
}
