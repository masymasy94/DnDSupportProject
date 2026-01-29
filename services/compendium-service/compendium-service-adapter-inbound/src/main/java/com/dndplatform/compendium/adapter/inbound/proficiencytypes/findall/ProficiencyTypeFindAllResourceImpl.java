package com.dndplatform.compendium.adapter.inbound.proficiencytypes.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.ProficiencyTypeFindAllResource;
import com.dndplatform.compendium.view.model.vm.ProficiencyTypeViewModel;
import io.quarkus.cache.CacheResult;
import io.smallrye.common.annotation.RunOnVirtualThread;
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
@RunOnVirtualThread
@Path("/api/compendium/proficiency-types")
@Tag(name = "Proficiency Types", description = "D&D proficiency type reference data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class ProficiencyTypeFindAllResourceImpl implements ProficiencyTypeFindAllResource {

    private final ProficiencyTypeFindAllResource delegate;

    @Inject
    public ProficiencyTypeFindAllResourceImpl(@Delegate ProficiencyTypeFindAllResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Override
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    @Operation(summary = "Get all proficiency types", description = "Retrieve all D&D proficiency types")
    @APIResponse(responseCode = "200", description = "Proficiency types list retrieved successfully")
    @CacheResult(cacheName = "proficiency-types-cache")
    public List<ProficiencyTypeViewModel> findAll() {
        return delegate.findAll();
    }
}
