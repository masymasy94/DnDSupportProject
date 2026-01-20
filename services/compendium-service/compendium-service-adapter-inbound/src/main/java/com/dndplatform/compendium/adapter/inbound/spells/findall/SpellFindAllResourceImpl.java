package com.dndplatform.compendium.adapter.inbound.spells.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.SpellFindAllResource;
import com.dndplatform.compendium.view.model.vm.SpellViewModel;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType.HTTP;

@RequestScoped
@Path("/api/compendium/spells")
@Tag(name = "Spells", description = "Spell reference data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SpellFindAllResourceImpl implements SpellFindAllResource {

    private final SpellFindAllResource delegate;

    @Inject
    public SpellFindAllResourceImpl(@Delegate SpellFindAllResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Override
    @Operation(summary = "Get all spells", description = "Retrieve all spells with optional filters")
    @APIResponse(responseCode = "200", description = "Spell list retrieved successfully")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public List<SpellViewModel> findAll(
            @Parameter(description = "Filter by spell level (0-9)")
            @QueryParam("level") Integer level,
            @Parameter(description = "Filter by school of magic")
            @QueryParam("school") String school,
            @Parameter(description = "Filter by concentration requirement")
            @QueryParam("concentration") Boolean concentration,
            @Parameter(description = "Filter by ritual casting")
            @QueryParam("ritual") Boolean ritual
    ) {
        return delegate.findAll(level, school, concentration, ritual);
    }
}
