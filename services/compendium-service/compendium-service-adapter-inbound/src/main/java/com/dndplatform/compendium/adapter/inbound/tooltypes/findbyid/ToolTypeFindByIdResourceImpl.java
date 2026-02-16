package com.dndplatform.compendium.adapter.inbound.tooltypes.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.ToolTypeFindByIdResource;
import com.dndplatform.compendium.view.model.vm.ToolTypeViewModel;
import io.quarkus.cache.CacheResult;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import static org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType.HTTP;

@RequestScoped
@RunOnVirtualThread
@Path("/api/compendium/tool-types")
@Tag(name = "Tool Types", description = "D&D tool type reference data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class ToolTypeFindByIdResourceImpl implements ToolTypeFindByIdResource {

    private final ToolTypeFindByIdResource delegate;

    @Inject
    public ToolTypeFindByIdResourceImpl(@Delegate ToolTypeFindByIdResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Path("/{id}")
    @Override
    @Operation(summary = "Get tool type by ID", description = "Retrieve a specific tool type by its ID")
    @APIResponse(responseCode = "200", description = "Tool type found")
    @APIResponse(responseCode = "404", description = "Tool type not found")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    @CacheResult(cacheName = "tool-type-by-id-cache")
    public ToolTypeViewModel findById(
            @Parameter(description = "Tool type ID", required = true, example = "1")
            @PathParam("id") int id) {
        return delegate.findById(id);
    }
}
