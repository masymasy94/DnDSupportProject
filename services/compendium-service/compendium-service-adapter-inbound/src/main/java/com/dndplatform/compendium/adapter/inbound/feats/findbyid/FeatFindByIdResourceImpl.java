package com.dndplatform.compendium.adapter.inbound.feats.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.FeatFindByIdResource;
import com.dndplatform.compendium.view.model.vm.FeatViewModel;
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
@Path("/api/compendium/feats")
@Tag(name = "Feats", description = "Feat reference data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class FeatFindByIdResourceImpl implements FeatFindByIdResource {

    private final FeatFindByIdResource delegate;

    @Inject
    public FeatFindByIdResourceImpl(@Delegate FeatFindByIdResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Path("/{id}")
    @Override
    @Operation(summary = "Get feat by ID", description = "Retrieve a specific feat by its ID")
    @APIResponse(responseCode = "200", description = "Feat found")
    @APIResponse(responseCode = "404", description = "Feat not found")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public FeatViewModel findById(
            @Parameter(description = "Feat ID", required = true, example = "1")
            @PathParam("id") int id) {
        return delegate.findById(id);
    }
}
