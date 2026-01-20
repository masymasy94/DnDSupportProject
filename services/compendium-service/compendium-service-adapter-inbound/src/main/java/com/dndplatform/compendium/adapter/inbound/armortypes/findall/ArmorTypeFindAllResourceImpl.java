package com.dndplatform.compendium.adapter.inbound.armortypes.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.ArmorTypeFindAllResource;
import com.dndplatform.compendium.view.model.vm.ArmorTypeViewModel;
import io.quarkus.cache.CacheResult;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

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
public class ArmorTypeFindAllResourceImpl implements ArmorTypeFindAllResource {

    private final ArmorTypeFindAllResource delegate;

    @Inject
    public ArmorTypeFindAllResourceImpl(@Delegate ArmorTypeFindAllResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Override
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    @Operation(summary = "Get all armor types", description = "Retrieve all D&D armor types")
    @APIResponse(responseCode = "200", description = "Armor types list retrieved successfully")
    @CacheResult(cacheName = "armor-types-cache")
    public List<ArmorTypeViewModel> findAll() {
        return delegate.findAll();
    }
}
