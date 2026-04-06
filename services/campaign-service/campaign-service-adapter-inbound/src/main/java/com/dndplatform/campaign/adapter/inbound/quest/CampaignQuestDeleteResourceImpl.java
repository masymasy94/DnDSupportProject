package com.dndplatform.campaign.adapter.inbound.quest;

import com.dndplatform.campaign.view.model.CampaignQuestDeleteResource;
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
@Path("/campaigns/{campaignId}/quests")
@Tag(name = "Campaign Quests", description = "Campaign quest operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CampaignQuestDeleteResourceImpl implements CampaignQuestDeleteResource {

    private final CampaignQuestDeleteDelegate delegate;

    @Inject
    public CampaignQuestDeleteResourceImpl(@Delegate CampaignQuestDeleteResource delegate) {
        this.delegate = (CampaignQuestDeleteDelegate) delegate;
    }

    @DELETE
    @Path("/{questId}")
    @Override
    @Operation(summary = "Delete a campaign quest", description = "Delete a quest (author or DM only)")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Quest deleted successfully"),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "403", description = "Forbidden"),
            @APIResponse(responseCode = "404", description = "Quest not found")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public void delete(
            @Parameter(description = "Campaign ID", required = true)
            @PathParam("campaignId") Long campaignId,
            @Parameter(description = "Quest ID", required = true)
            @PathParam("questId") Long questId,
            @Parameter(description = "User ID", required = true)
            @QueryParam("userId") Long userId) {

        delegate.delete(campaignId, questId, userId);
    }
}
