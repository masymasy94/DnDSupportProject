package com.dndplatform.campaign.adapter.inbound.quest;

import com.dndplatform.campaign.view.model.CampaignQuestUpdateResource;
import com.dndplatform.campaign.view.model.vm.CampaignQuestViewModel;
import com.dndplatform.campaign.view.model.vm.UpdateQuestRequest;
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
@Path("/campaigns/{campaignId}/quests")
@Tag(name = "Campaign Quests", description = "Campaign quest operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CampaignQuestUpdateResourceImpl implements CampaignQuestUpdateResource {

    private final CampaignQuestUpdateDelegate delegate;

    @Inject
    public CampaignQuestUpdateResourceImpl(@Delegate CampaignQuestUpdateResource delegate) {
        this.delegate = (CampaignQuestUpdateDelegate) delegate;
    }

    @PUT
    @Path("/{questId}")
    @Override
    @Operation(summary = "Update a campaign quest", description = "Update a quest (author or DM only)")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Quest updated successfully",
                    content = @Content(schema = @Schema(implementation = CampaignQuestViewModel.class))
            ),
            @APIResponse(responseCode = "400", description = "Invalid request"),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "403", description = "Forbidden - not the author or DM"),
            @APIResponse(responseCode = "404", description = "Quest not found")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public CampaignQuestViewModel update(
            @Parameter(description = "Campaign ID", required = true)
            @PathParam("campaignId") Long campaignId,
            @Parameter(description = "Quest ID", required = true)
            @PathParam("questId") Long questId,
            @RequestBody(
                    description = "Quest update details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateQuestRequest.class))
            )
            @Valid UpdateQuestRequest request) {

        return delegate.update(campaignId, questId, request);
    }
}
