package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.view.model.TurnOrderFindResource;
import com.dndplatform.combat.view.model.vm.ParticipantViewModel;
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

import java.util.List;

@RequestScoped
@RunOnVirtualThread
@Path("/encounters")
@Tag(name = "Encounters", description = "Encounter management operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TurnOrderFindResourceImpl implements TurnOrderFindResource {

    private final TurnOrderFindDelegate delegate;

    @Inject
    public TurnOrderFindResourceImpl(@Delegate TurnOrderFindResource delegate) {
        this.delegate = (TurnOrderFindDelegate) delegate;
    }

    @GET
    @Path("/{id}/turns")
    @Override
    @Operation(summary = "Get turn order", description = "Get the current turn order for an encounter")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Turn order retrieved successfully"),
            @APIResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public List<ParticipantViewModel> getTurnOrder(
            @Parameter(description = "Encounter ID", required = true, example = "1")
            @PathParam("id") Long encounterId) {
        return delegate.getTurnOrder(encounterId);
    }
}
