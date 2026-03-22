package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.view.model.EncounterDeleteResource;
import com.dndplatform.common.annotations.Delegate;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@RequestScoped
@RunOnVirtualThread
@Path("/encounters")
@Tag(name = "Encounters", description = "Encounter management operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EncounterDeleteResourceImpl implements EncounterDeleteResource {

    private final EncounterDeleteDelegate delegate;

    @Inject
    public EncounterDeleteResourceImpl(@Delegate EncounterDeleteResource delegate) {
        this.delegate = (EncounterDeleteDelegate) delegate;
    }

    @DELETE
    @Path("/{id}")
    @Override
    @Operation(summary = "Delete an encounter", description = "Delete an encounter. Only the DM can delete.")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Encounter deleted successfully"),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "403", description = "Forbidden - not the DM"),
            @APIResponse(responseCode = "404", description = "Encounter not found")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public void delete(
            @Parameter(description = "Encounter ID", required = true, example = "1")
            @PathParam("id") Long id,
            @Parameter(description = "User ID", required = true)
            @QueryParam("userId") Long userId) {
        delegate.delete(id, userId);
    }
}
