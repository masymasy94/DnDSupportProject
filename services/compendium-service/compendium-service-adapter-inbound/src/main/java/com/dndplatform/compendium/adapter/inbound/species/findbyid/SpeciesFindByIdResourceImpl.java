package com.dndplatform.compendium.adapter.inbound.species.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.SpeciesFindByIdResource;
import io.smallrye.common.annotation.RunOnVirtualThread;
import com.dndplatform.compendium.view.model.vm.SpeciesViewModel;
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
@Path("/api/compendium/species")
@Tag(name = "Species", description = "Species/Race reference data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SpeciesFindByIdResourceImpl implements SpeciesFindByIdResource {

    private final SpeciesFindByIdResource delegate;

    @Inject
    public SpeciesFindByIdResourceImpl(@Delegate SpeciesFindByIdResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Path("/{id}")
    @Override
    @Operation(summary = "Get species by ID", description = "Retrieve a specific species by its ID")
    @APIResponse(responseCode = "200", description = "Species found")
    @APIResponse(responseCode = "404", description = "Species not found")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public SpeciesViewModel findById(
            @Parameter(description = "Species ID", required = true, example = "1")
            @PathParam("id") int id) {
        return delegate.findById(id);
    }
}
