package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.view.model.InitiativeStartResource;
import com.dndplatform.combat.view.model.vm.EncounterViewModel;
import com.dndplatform.common.annotations.Delegate;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
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
public class InitiativeStartResourceImpl implements InitiativeStartResource {

    private final InitiativeStartDelegate delegate;

    @Inject
    public InitiativeStartResourceImpl(@Delegate InitiativeStartResource delegate) {
        this.delegate = (InitiativeStartDelegate) delegate;
    }

    @POST
    @Path("/{id}/initiative")
    @Override
    @Operation(summary = "Start initiative", description = "Roll initiative for all participants and start combat")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Initiative started successfully",
                    content = @Content(schema = @Schema(implementation = EncounterViewModel.class))
            ),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "403", description = "Forbidden"),
            @APIResponse(responseCode = "404", description = "Encounter not found")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public EncounterViewModel start(
            @Parameter(description = "Encounter ID", required = true, example = "1")
            @PathParam("id") Long encounterId,
            @Parameter(description = "User ID", required = true)
            @QueryParam("userId") Long userId) {
        return delegate.start(encounterId, userId);
    }
}
