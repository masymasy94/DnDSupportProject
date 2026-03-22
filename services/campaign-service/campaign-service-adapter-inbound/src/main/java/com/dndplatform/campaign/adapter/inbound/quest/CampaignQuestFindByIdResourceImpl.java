package com.dndplatform.campaign.adapter.inbound.quest;

import com.dndplatform.campaign.view.model.CampaignQuestFindByIdResource;
import com.dndplatform.campaign.view.model.vm.CampaignQuestViewModel;
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
@Path("/campaigns/{campaignId}/quests")
@Tag(name = "Campaign Quests", description = "Campaign quest operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CampaignQuestFindByIdResourceImpl implements CampaignQuestFindByIdResource {

    private final CampaignQuestFindByIdDelegate delegate;

    @Inject
    public CampaignQuestFindByIdResourceImpl(@Delegate CampaignQuestFindByIdResource delegate) {
        this.delegate = (CampaignQuestFindByIdDelegate) delegate;
    }

    @GET
    @Path("/{questId}")
    @Override
    @Operation(summary = "Get a campaign quest", description = "Get a single quest by ID")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Quest retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CampaignQuestViewModel.class))
            ),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "404", description = "Quest not found")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public CampaignQuestViewModel findById(
            @Parameter(description = "Campaign ID", required = true)
            @PathParam("campaignId") Long campaignId,
            @Parameter(description = "Quest ID", required = true)
            @PathParam("questId") Long questId,
            @Parameter(description = "User ID", required = true)
            @QueryParam("userId") Long userId) {

        return delegate.findById(campaignId, questId, userId);
    }
}
