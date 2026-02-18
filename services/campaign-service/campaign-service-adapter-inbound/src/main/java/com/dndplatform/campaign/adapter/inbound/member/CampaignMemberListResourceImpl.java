package com.dndplatform.campaign.adapter.inbound.member;

import com.dndplatform.campaign.view.model.CampaignMemberListResource;
import com.dndplatform.campaign.view.model.vm.CampaignMemberViewModel;
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
@Path("/campaigns/{campaignId}/members")
@Tag(name = "Campaign Members", description = "Campaign membership operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CampaignMemberListResourceImpl implements CampaignMemberListResource {

    private final CampaignMemberListDelegate delegate;

    @Inject
    public CampaignMemberListResourceImpl(@Delegate CampaignMemberListResource delegate) {
        this.delegate = (CampaignMemberListDelegate) delegate;
    }

    @GET
    @Override
    @Operation(summary = "List campaign members", description = "Retrieve all members of a campaign")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Members retrieved successfully"),
            @APIResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public List<CampaignMemberViewModel> listMembers(
            @Parameter(description = "Campaign ID", required = true)
            @PathParam("campaignId") Long campaignId) {
        return delegate.listMembers(campaignId);
    }
}
