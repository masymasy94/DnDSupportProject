package com.dndplatform.campaign.adapter.inbound.find;

import com.dndplatform.campaign.view.model.CampaignFindByIdResource;
import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
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
public class CampaignFindByIdResourceImpl implements CampaignFindByIdResource {

    private final CampaignFindByIdDelegate delegate;

    @Inject
    public CampaignFindByIdResourceImpl(@Delegate CampaignFindByIdResource delegate) {
        this.delegate = (CampaignFindByIdDelegate) delegate;
    }

    @GET
    @Path("/{id}")
    @Override
    @Operation(summary = "Get campaign details", description = "Retrieve full details of a campaign by ID")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Campaign retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CampaignViewModel.class))
            ),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "404", description = "Campaign not found")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public CampaignViewModel findById(
            @Parameter(description = "Campaign ID", required = true, example = "1")
            @PathParam("id") Long id) {
        return delegate.findById(id);
    }
}
