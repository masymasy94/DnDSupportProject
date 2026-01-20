package com.dndplatform.compendium.adapter.inbound.weapontypes.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.WeaponTypeFindAllResource;
import com.dndplatform.compendium.view.model.vm.WeaponTypeViewModel;
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

import java.util.List;

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
public class WeaponTypeFindAllResourceImpl implements WeaponTypeFindAllResource {

    private final WeaponTypeFindAllResource delegate;

    @Inject
    public WeaponTypeFindAllResourceImpl(@Delegate WeaponTypeFindAllResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Override
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    @Operation(summary = "Get all weapon types", description = "Retrieve all D&D weapon types, optionally filtered by category")
    @APIResponse(responseCode = "200", description = "Weapon types list retrieved successfully")
    public List<WeaponTypeViewModel> findAll(
            @Parameter(description = "Filter by category (SIMPLE, MARTIAL)", example = "MARTIAL")
            @QueryParam("category") String category) {
        return delegate.findAll(category);
    }
}
