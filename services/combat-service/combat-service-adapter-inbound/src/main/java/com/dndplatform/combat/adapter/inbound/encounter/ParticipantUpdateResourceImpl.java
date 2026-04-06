package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.view.model.ParticipantUpdateResource;
import com.dndplatform.combat.view.model.vm.ParticipantViewModel;
import com.dndplatform.combat.view.model.vm.UpdateParticipantRequest;
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
public class ParticipantUpdateResourceImpl implements ParticipantUpdateResource {

    private final ParticipantUpdateDelegate delegate;

    @Inject
    public ParticipantUpdateResourceImpl(@Delegate ParticipantUpdateResource delegate) {
        this.delegate = (ParticipantUpdateDelegate) delegate;
    }

    @PUT
    @Path("/{id}/participants/{pid}")
    @Override
    @Operation(summary = "Update a participant", description = "Update a participant's HP, conditions, or active state")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Participant updated successfully",
                    content = @Content(schema = @Schema(implementation = ParticipantViewModel.class))
            ),
            @APIResponse(responseCode = "400", description = "Invalid request"),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "403", description = "Forbidden"),
            @APIResponse(responseCode = "404", description = "Encounter not found")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public ParticipantViewModel update(
            @Parameter(description = "Encounter ID", required = true, example = "1")
            @PathParam("id") Long encounterId,
            @Parameter(description = "Participant ID", required = true, example = "1")
            @PathParam("pid") Long participantId,
            @RequestBody(
                    description = "Participant update details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateParticipantRequest.class))
            )
            @Valid UpdateParticipantRequest request) {
        return delegate.update(encounterId, participantId, request);
    }
}
