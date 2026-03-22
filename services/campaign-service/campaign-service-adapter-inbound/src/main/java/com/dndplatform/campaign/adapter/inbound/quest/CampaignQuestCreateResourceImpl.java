package com.dndplatform.campaign.adapter.inbound.quest;

import com.dndplatform.campaign.view.model.CampaignQuestCreateResource;
import com.dndplatform.campaign.view.model.vm.CampaignQuestViewModel;
import com.dndplatform.campaign.view.model.vm.CreateQuestRequest;
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
public class CampaignQuestCreateResourceImpl implements CampaignQuestCreateResource {

    private final CampaignQuestCreateDelegate delegate;

    @Inject
    public CampaignQuestCreateResourceImpl(@Delegate CampaignQuestCreateResource delegate) {
        this.delegate = (CampaignQuestCreateDelegate) delegate;
    }

    @POST
    @Override
    @Operation(summary = "Create a campaign quest", description = "Create a quest in the campaign")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Quest created successfully",
                    content = @Content(schema = @Schema(implementation = CampaignQuestViewModel.class))
            ),
            @APIResponse(responseCode = "400", description = "Invalid request"),
            @APIResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public CampaignQuestViewModel create(
            @Parameter(description = "Campaign ID", required = true)
            @PathParam("campaignId") Long campaignId,
            @RequestBody(
                    description = "Quest details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateQuestRequest.class))
            )
            @Valid CreateQuestRequest request) {

        return delegate.create(campaignId, request);
    }
}
