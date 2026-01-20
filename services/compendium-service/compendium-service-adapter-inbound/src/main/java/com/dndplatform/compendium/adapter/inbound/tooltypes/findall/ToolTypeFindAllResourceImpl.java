package com.dndplatform.compendium.adapter.inbound.tooltypes.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.ToolTypeFindAllResource;
import com.dndplatform.compendium.view.model.vm.ToolTypeViewModel;
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

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType.HTTP;

@RequestScoped
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
public class ToolTypeFindAllResourceImpl implements ToolTypeFindAllResource {

    private final ToolTypeFindAllResource delegate;

    @Inject
    public ToolTypeFindAllResourceImpl(@Delegate ToolTypeFindAllResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Override
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    @Operation(summary = "Get all tool types", description = "Retrieve all D&D tool types, optionally filtered by category")
    @APIResponse(responseCode = "200", description = "Tool types list retrieved successfully")
    public List<ToolTypeViewModel> findAll(
            @Parameter(description = "Filter by category (ARTISAN, GAMING, INSTRUMENT, OTHER)", example = "ARTISAN")
            @QueryParam("category") String category) {
        return delegate.findAll(category);
    }
}
