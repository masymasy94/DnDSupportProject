package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.view.model.EncounterUpdateResource;
import com.dndplatform.combat.view.model.vm.EncounterViewModel;
import com.dndplatform.combat.view.model.vm.UpdateEncounterRequest;
import com.dndplatform.common.annotations.Delegate;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
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
public class EncounterUpdateResourceImpl implements EncounterUpdateResource {

    private final EncounterUpdateDelegate delegate;

    @Inject
    public EncounterUpdateResourceImpl(@Delegate EncounterUpdateResource delegate) {
        this.delegate = (EncounterUpdateDelegate) delegate;
    }

    @PUT
    @Path("/{id}")
    @Override
    @Operation(summary = "Update an encounter", description = "Update an existing encounter. Only the DM can update.")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Encounter updated successfully",
                    content = @Content(schema = @Schema(implementation = EncounterViewModel.class))
            ),
            @APIResponse(responseCode = "400", description = "Invalid request"),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "403", description = "Forbidden - not the DM"),
            @APIResponse(responseCode = "404", description = "Encounter not found")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public EncounterViewModel update(
            @Parameter(description = "Encounter ID", required = true, example = "1")
            @PathParam("id") Long id,
            @RequestBody(
                    description = "Encounter update details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateEncounterRequest.class))
            )
            @Valid UpdateEncounterRequest request) {
        return delegate.update(id, request);
    }
}
