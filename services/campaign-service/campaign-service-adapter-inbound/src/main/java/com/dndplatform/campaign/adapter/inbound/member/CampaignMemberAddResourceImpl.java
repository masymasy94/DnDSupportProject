package com.dndplatform.campaign.adapter.inbound.member;

import com.dndplatform.campaign.view.model.CampaignMemberAddResource;
import com.dndplatform.campaign.view.model.vm.AddMemberRequest;
import com.dndplatform.campaign.view.model.vm.CampaignMemberViewModel;
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
@Path("/campaigns/{campaignId}/members")
@Tag(name = "Campaign Members", description = "Campaign membership operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CampaignMemberAddResourceImpl implements CampaignMemberAddResource {

    private final CampaignMemberAddDelegate delegate;

    @Inject
    public CampaignMemberAddResourceImpl(@Delegate CampaignMemberAddResource delegate) {
        this.delegate = (CampaignMemberAddDelegate) delegate;
    }

    @POST
    @Override
    @Operation(summary = "Add a member to the campaign", description = "Add a player to the campaign. Only the DM can add members.")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Member added successfully",
                    content = @Content(schema = @Schema(implementation = CampaignMemberViewModel.class))
            ),
            @APIResponse(responseCode = "400", description = "Invalid request"),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "403", description = "Forbidden - not the Dungeon Master"),
            @APIResponse(responseCode = "404", description = "Campaign not found")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public CampaignMemberViewModel addMember(
            @Parameter(description = "Campaign ID", required = true)
            @PathParam("campaignId") Long campaignId,
            @RequestBody(
                    description = "Member details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AddMemberRequest.class))
            )
            @Valid AddMemberRequest request) {

        return delegate.addMember(campaignId, request);
    }
}
