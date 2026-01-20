package com.dndplatform.compendium.adapter.inbound.spellschools.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.SpellSchoolFindByIdResource;
import com.dndplatform.compendium.view.model.vm.SpellSchoolViewModel;
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
public class SpellSchoolFindByIdResourceImpl implements SpellSchoolFindByIdResource {

    private final SpellSchoolFindByIdResource delegate;

    @Inject
    public SpellSchoolFindByIdResourceImpl(@Delegate SpellSchoolFindByIdResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Path("/{id}")
    @Override
    @Operation(summary = "Get spell school by ID", description = "Retrieve a specific spell school by its ID")
    @APIResponse(responseCode = "200", description = "Spell school found")
    @APIResponse(responseCode = "404", description = "Spell school not found")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    @CacheResult(cacheName = "spell-school-by-id-cache")
    public SpellSchoolViewModel findById(
            @Parameter(description = "Spell school ID", required = true, example = "1")
            @PathParam("id") int id) {
        return delegate.findById(id);
    }
}
