package com.dndplatform.campaign.adapter.inbound.find;

import com.dndplatform.campaign.view.model.CampaignFindAllResource;
import com.dndplatform.campaign.view.model.vm.PagedCampaignsViewModel;
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
@Path("/campaigns")
@Tag(name = "Campaign", description = "Campaign management operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CampaignFindAllResourceImpl implements CampaignFindAllResource {

    private final CampaignFindAllDelegate delegate;

    @Inject
    public CampaignFindAllResourceImpl(@Delegate CampaignFindAllResource delegate) {
        this.delegate = (CampaignFindAllDelegate) delegate;
    }

    @GET
    @Override
    @Operation(summary = "List user's campaigns", description = "Retrieve a paginated list of campaigns the user is a member of")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Campaigns retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PagedCampaignsViewModel.class))
            ),
            @APIResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public PagedCampaignsViewModel findAll(
            @Parameter(description = "User ID", required = true)
            @QueryParam("userId") Long userId,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @QueryParam("page") @DefaultValue("0") int page,
            @Parameter(description = "Page size", example = "20")
            @QueryParam("size") @DefaultValue("20") int size) {

        return delegate.findAll(userId, page, size);
    }
}
