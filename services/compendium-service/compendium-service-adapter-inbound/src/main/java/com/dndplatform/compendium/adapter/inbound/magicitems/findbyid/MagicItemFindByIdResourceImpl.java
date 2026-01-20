package com.dndplatform.compendium.adapter.inbound.magicitems.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.MagicItemFindByIdResource;
import com.dndplatform.compendium.view.model.vm.MagicItemViewModel;
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
@Path("/api/compendium/magic-items")
@Tag(name = "Magic Items", description = "Magic item reference data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class MagicItemFindByIdResourceImpl implements MagicItemFindByIdResource {

    private final MagicItemFindByIdResource delegate;

    @Inject
    public MagicItemFindByIdResourceImpl(@Delegate MagicItemFindByIdResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Path("/{id}")
    @Override
    @Operation(summary = "Get magic item by ID", description = "Retrieve a specific magic item by its ID")
    @APIResponse(responseCode = "200", description = "Magic item found")
    @APIResponse(responseCode = "404", description = "Magic item not found")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public MagicItemViewModel findById(
            @Parameter(description = "Magic item ID", required = true, example = "1")
            @PathParam("id") int id) {
        return delegate.findById(id);
    }
}
