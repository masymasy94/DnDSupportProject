package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.view.model.EncounterFindByCampaignResource;
import com.dndplatform.combat.view.model.vm.EncounterViewModel;
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
public class EncounterFindByCampaignResourceImpl implements EncounterFindByCampaignResource {

    private final EncounterFindByCampaignDelegate delegate;

    @Inject
    public EncounterFindByCampaignResourceImpl(@Delegate EncounterFindByCampaignResource delegate) {
        this.delegate = (EncounterFindByCampaignDelegate) delegate;
    }

    @GET
    @Override
    @Operation(summary = "List encounters by campaign", description = "Retrieve all encounters for a campaign")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Encounters retrieved successfully"),
            @APIResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public List<EncounterViewModel> findByCampaign(
            @Parameter(description = "Campaign ID", required = true)
            @QueryParam("campaignId") Long campaignId,
            @Parameter(description = "User ID")
            @QueryParam("userId") Long userId) {
        return delegate.findByCampaign(campaignId, userId);
    }
}
