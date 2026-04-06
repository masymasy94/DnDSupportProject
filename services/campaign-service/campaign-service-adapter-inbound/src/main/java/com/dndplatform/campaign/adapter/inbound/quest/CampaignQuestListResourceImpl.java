package com.dndplatform.campaign.adapter.inbound.quest;

import com.dndplatform.campaign.view.model.CampaignQuestListResource;
import com.dndplatform.campaign.view.model.vm.CampaignQuestViewModel;
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
@Path("/campaigns/{campaignId}/quests")
@Tag(name = "Campaign Quests", description = "Campaign quest operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CampaignQuestListResourceImpl implements CampaignQuestListResource {

    private final CampaignQuestListDelegate delegate;

    @Inject
    public CampaignQuestListResourceImpl(@Delegate CampaignQuestListResource delegate) {
        this.delegate = (CampaignQuestListDelegate) delegate;
    }

    @GET
    @Override
    @Operation(summary = "List campaign quests", description = "List quests in a campaign, optionally filtered by status")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Quests retrieved successfully"),
            @APIResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public List<CampaignQuestViewModel> list(
            @Parameter(description = "Campaign ID", required = true)
            @PathParam("campaignId") Long campaignId,
            @Parameter(description = "User ID", required = true)
            @QueryParam("userId") Long userId,
            @Parameter(description = "Filter by status (ACTIVE, COMPLETED, FAILED)")
            @QueryParam("status") String status) {

        return delegate.list(campaignId, userId, status);
    }
}
