package com.dndplatform.compendium.adapter.inbound.damagetypes.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.DamageTypeFindByIdResource;
import com.dndplatform.compendium.view.model.vm.DamageTypeViewModel;
import io.quarkus.cache.CacheResult;
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
@Path("/api/compendium/damage-types")
@Tag(name = "Damage Types", description = "D&D damage type reference data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class DamageTypeFindByIdResourceImpl implements DamageTypeFindByIdResource {

    private final DamageTypeFindByIdResource delegate;

    @Inject
    public DamageTypeFindByIdResourceImpl(@Delegate DamageTypeFindByIdResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Path("/{id}")
    @Override
    @Operation(summary = "Get damage type by ID", description = "Retrieve a specific damage type by its ID")
    @APIResponse(responseCode = "200", description = "Damage type found")
    @APIResponse(responseCode = "404", description = "Damage type not found")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    @CacheResult(cacheName = "damage-type-by-id-cache")
    public DamageTypeViewModel findById(
            @Parameter(description = "Damage type ID", required = true, example = "1")
            @PathParam("id") int id) {
        return delegate.findById(id);
    }
}
