package com.dndplatform.compendium.adapter.inbound.spellschools.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.SpellSchoolFindAllResource;
import com.dndplatform.compendium.view.model.vm.SpellSchoolViewModel;
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
@Path("/api/compendium/spell-schools")
@Tag(name = "Spell Schools", description = "D&D spell school reference data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SpellSchoolFindAllResourceImpl implements SpellSchoolFindAllResource {

    private final SpellSchoolFindAllResource delegate;

    @Inject
    public SpellSchoolFindAllResourceImpl(@Delegate SpellSchoolFindAllResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Override
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    @Operation(summary = "Get all spell schools", description = "Retrieve all D&D spell schools")
    @APIResponse(responseCode = "200", description = "Spell schools list retrieved successfully")
    @CacheResult(cacheName = "spell-schools-cache")
    public List<SpellSchoolViewModel> findAll() {
        return delegate.findAll();
    }
}
