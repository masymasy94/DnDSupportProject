package com.dndplatform.compendium.adapter.inbound.weapontypes.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.WeaponTypeFindByIdResource;
import com.dndplatform.compendium.view.model.vm.WeaponTypeViewModel;
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
@Path("/api/compendium/weapon-types")
@Tag(name = "Weapon Types", description = "D&D weapon type reference data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class WeaponTypeFindByIdResourceImpl implements WeaponTypeFindByIdResource {

    private final WeaponTypeFindByIdResource delegate;

    @Inject
    public WeaponTypeFindByIdResourceImpl(@Delegate WeaponTypeFindByIdResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Path("/{id}")
    @Override
    @Operation(summary = "Get weapon type by ID", description = "Retrieve a specific weapon type by its ID")
    @APIResponse(responseCode = "200", description = "Weapon type found")
    @APIResponse(responseCode = "404", description = "Weapon type not found")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    @CacheResult(cacheName = "weapon-type-by-id-cache")
    public WeaponTypeViewModel findById(
            @Parameter(description = "Weapon type ID", required = true, example = "1")
            @PathParam("id") int id) {
        return delegate.findById(id);
    }
}
