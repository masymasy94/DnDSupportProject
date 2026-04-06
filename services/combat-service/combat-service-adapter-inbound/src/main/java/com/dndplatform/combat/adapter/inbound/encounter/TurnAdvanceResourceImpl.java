package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.view.model.TurnAdvanceResource;
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
public class TurnAdvanceResourceImpl implements TurnAdvanceResource {

    private final TurnAdvanceDelegate delegate;

    @Inject
    public TurnAdvanceResourceImpl(@Delegate TurnAdvanceResource delegate) {
        this.delegate = (TurnAdvanceDelegate) delegate;
    }

    @PUT
    @Path("/{id}/turns/next")
    @Override
    @Operation(summary = "Advance turn", description = "Advance to the next participant in turn order")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Turn advanced successfully"),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "403", description = "Forbidden"),
            @APIResponse(responseCode = "404", description = "Encounter not found")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public List<ParticipantViewModel> advance(
            @Parameter(description = "Encounter ID", required = true, example = "1")
            @PathParam("id") Long encounterId,
            @Parameter(description = "User ID", required = true)
            @QueryParam("userId") Long userId) {
        return delegate.advance(encounterId, userId);
    }
}
