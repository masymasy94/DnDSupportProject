package com.dndplatform.compendium.adapter.inbound.species.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.SpeciesFindAllResource;
import com.dndplatform.compendium.view.model.vm.SpeciesViewModel;
import io.quarkus.cache.CacheResult;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType.HTTP;

@RequestScoped
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
public class SpeciesFindAllResourceImpl implements SpeciesFindAllResource {

    private final SpeciesFindAllResource delegate;

    @Inject
    public SpeciesFindAllResourceImpl(@Delegate SpeciesFindAllResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Override
    @Operation(summary = "Get all species", description = "Retrieve all species/races")
    @APIResponse(responseCode = "200", description = "Species list retrieved successfully")
    @CacheResult(cacheName = "species-cache")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public List<SpeciesViewModel> findAll() {
        return delegate.findAll();
    }
}
