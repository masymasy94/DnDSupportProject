package com.dndplatform.compendium.adapter.inbound.equipment.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.EquipmentFindByIdResource;
import com.dndplatform.compendium.view.model.vm.EquipmentViewModel;
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
@Path("/api/compendium/equipment")
@Tag(name = "Equipment", description = "Equipment reference data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class EquipmentFindByIdResourceImpl implements EquipmentFindByIdResource {

    private final EquipmentFindByIdResource delegate;

    @Inject
    public EquipmentFindByIdResourceImpl(@Delegate EquipmentFindByIdResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Path("/{id}")
    @Override
    @Operation(summary = "Get equipment by ID", description = "Retrieve a specific equipment item by its ID")
    @APIResponse(responseCode = "200", description = "Equipment found")
    @APIResponse(responseCode = "404", description = "Equipment not found")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public EquipmentViewModel findById(
            @Parameter(description = "Equipment ID", required = true, example = "1")
            @PathParam("id") int id) {
        return delegate.findById(id);
    }
}
