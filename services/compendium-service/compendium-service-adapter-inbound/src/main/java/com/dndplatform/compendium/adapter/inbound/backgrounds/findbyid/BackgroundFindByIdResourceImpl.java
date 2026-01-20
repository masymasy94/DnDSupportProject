package com.dndplatform.compendium.adapter.inbound.backgrounds.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.BackgroundFindByIdResource;
import com.dndplatform.compendium.view.model.vm.BackgroundViewModel;
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
@Path("/api/compendium/backgrounds")
@Tag(name = "Backgrounds", description = "D&D background reference data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class BackgroundFindByIdResourceImpl implements BackgroundFindByIdResource {

    private final BackgroundFindByIdResource delegate;

    @Inject
    public BackgroundFindByIdResourceImpl(@Delegate BackgroundFindByIdResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Path("/{id}")
    @Override
    @Operation(summary = "Get background by ID", description = "Retrieve a specific background by its ID")
    @APIResponse(responseCode = "200", description = "Background found")
    @APIResponse(responseCode = "404", description = "Background not found")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public BackgroundViewModel findById(
            @Parameter(description = "Background ID", required = true, example = "1")
            @PathParam("id") int id) {
        return delegate.findById(id);
    }
}
