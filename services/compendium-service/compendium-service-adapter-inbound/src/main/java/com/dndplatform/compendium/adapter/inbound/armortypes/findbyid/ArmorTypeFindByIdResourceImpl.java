package com.dndplatform.compendium.adapter.inbound.armortypes.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.ArmorTypeFindByIdResource;
import com.dndplatform.compendium.view.model.vm.ArmorTypeViewModel;
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
@Path("/api/compendium/armor-types")
@Tag(name = "Armor Types", description = "D&D armor type reference data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class ArmorTypeFindByIdResourceImpl implements ArmorTypeFindByIdResource {

    private final ArmorTypeFindByIdResource delegate;

    @Inject
    public ArmorTypeFindByIdResourceImpl(@Delegate ArmorTypeFindByIdResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Path("/{id}")
    @Override
    @Operation(summary = "Get armor type by ID", description = "Retrieve a specific armor type by its ID")
    @APIResponse(responseCode = "200", description = "Armor type found")
    @APIResponse(responseCode = "404", description = "Armor type not found")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    @CacheResult(cacheName = "armor-type-by-id-cache")
    public ArmorTypeViewModel findById(
            @Parameter(description = "Armor type ID", required = true, example = "1")
            @PathParam("id") int id) {
        return delegate.findById(id);
    }
}
