package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.view.model.ParticipantAddResource;
import com.dndplatform.combat.view.model.vm.AddParticipantRequest;
import com.dndplatform.combat.view.model.vm.ParticipantViewModel;
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
public class ParticipantAddResourceImpl implements ParticipantAddResource {

    private final ParticipantAddDelegate delegate;

    @Inject
    public ParticipantAddResourceImpl(@Delegate ParticipantAddResource delegate) {
        this.delegate = (ParticipantAddDelegate) delegate;
    }

    @POST
    @Path("/{id}/participants")
    @Override
    @Operation(summary = "Add a participant", description = "Add a participant to an encounter")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Participant added successfully",
                    content = @Content(schema = @Schema(implementation = ParticipantViewModel.class))
            ),
            @APIResponse(responseCode = "400", description = "Invalid request"),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "403", description = "Forbidden"),
            @APIResponse(responseCode = "404", description = "Encounter not found")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public ParticipantViewModel add(
            @Parameter(description = "Encounter ID", required = true, example = "1")
            @PathParam("id") Long encounterId,
            @RequestBody(
                    description = "Participant details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AddParticipantRequest.class))
            )
            @Valid AddParticipantRequest request) {
        return delegate.add(encounterId, request);
    }
}
