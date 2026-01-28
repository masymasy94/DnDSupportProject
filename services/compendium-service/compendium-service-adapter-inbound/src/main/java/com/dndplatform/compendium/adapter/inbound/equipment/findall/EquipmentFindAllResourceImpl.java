package com.dndplatform.compendium.adapter.inbound.equipment.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.EquipmentFindAllResource;
import io.smallrye.common.annotation.RunOnVirtualThread;
import com.dndplatform.compendium.view.model.vm.PagedEquipmentViewModel;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
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

import static org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType.HTTP;

@RequestScoped
@RunOnVirtualThread
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
public class EquipmentFindAllResourceImpl implements EquipmentFindAllResource {

    private final EquipmentFindAllResource delegate;

    @Inject
    public EquipmentFindAllResourceImpl(@Delegate EquipmentFindAllResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Override
    @Operation(summary = "Get all equipment", description = "Retrieve all equipment with optional filters and pagination")
    @APIResponse(responseCode = "200", description = "Equipment list retrieved successfully")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public PagedEquipmentViewModel findAll(
            @Parameter(description = "Filter by equipment name (partial match)")
            @QueryParam("name") String name,

            @Parameter(description = "Filter by category (Simple Weapons, Martial Weapons, Armor, Adventuring Gear, Tools, etc.)")
            @QueryParam("category") String category,

            @Parameter(description = "Page number (0-indexed)")
            @QueryParam("page") @DefaultValue("0") Integer page,

            @Parameter(description = "Page size (default 50)")
            @QueryParam("pageSize") @DefaultValue("50") Integer pageSize
    ) {
        return delegate.findAll(name, category, page, pageSize);
    }
}
