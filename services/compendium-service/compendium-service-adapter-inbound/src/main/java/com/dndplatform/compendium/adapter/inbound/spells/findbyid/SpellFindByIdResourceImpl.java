package com.dndplatform.compendium.adapter.inbound.spells.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.SpellFindByIdResource;
import io.smallrye.common.annotation.RunOnVirtualThread;
import com.dndplatform.compendium.view.model.vm.SpellViewModel;
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
@RunOnVirtualThread
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
public class SpellFindByIdResourceImpl implements SpellFindByIdResource {

    private final SpellFindByIdResource delegate;

    @Inject
    public SpellFindByIdResourceImpl(@Delegate SpellFindByIdResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Path("/{id}")
    @Override
    @Operation(summary = "Get spell by ID", description = "Retrieve a specific spell by its ID")
    @APIResponse(responseCode = "200", description = "Spell found")
    @APIResponse(responseCode = "404", description = "Spell not found")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public SpellViewModel findById(
            @Parameter(description = "Spell ID", required = true, example = "1")
            @PathParam("id") int id) {
        return delegate.findById(id);
    }
}
