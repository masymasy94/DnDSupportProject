package com.dndplatform.campaign.adapter.inbound.member;

import com.dndplatform.campaign.view.model.CampaignMemberRemoveResource;
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
@Path("/campaigns/{campaignId}/members")
@Tag(name = "Campaign Members", description = "Campaign membership operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CampaignMemberRemoveResourceImpl implements CampaignMemberRemoveResource {

    private final CampaignMemberRemoveDelegate delegate;

    @Inject
    public CampaignMemberRemoveResourceImpl(@Delegate CampaignMemberRemoveResource delegate) {
        this.delegate = (CampaignMemberRemoveDelegate) delegate;
    }

    @DELETE
    @Path("/{userId}")
    @Override
    @Operation(summary = "Remove a member", description = "Remove a member from the campaign. DM or self-leave only.")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Member removed successfully"),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "403", description = "Forbidden"),
            @APIResponse(responseCode = "404", description = "Campaign or member not found")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public void removeMember(
            @Parameter(description = "Campaign ID", required = true)
            @PathParam("campaignId") Long campaignId,
            @Parameter(description = "User ID of the member to remove", required = true)
            @PathParam("userId") Long userId,
            @Parameter(description = "Requester ID", required = true)
            @QueryParam("requesterId") Long requesterId) {

        delegate.removeMember(campaignId, userId, requesterId);
    }
}
