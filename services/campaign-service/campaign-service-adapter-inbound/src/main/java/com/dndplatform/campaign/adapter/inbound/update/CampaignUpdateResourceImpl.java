package com.dndplatform.campaign.adapter.inbound.update;

import com.dndplatform.campaign.view.model.CampaignUpdateResource;
import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
import com.dndplatform.campaign.view.model.vm.UpdateCampaignRequest;
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
@Path("/campaigns")
@Tag(name = "Campaign", description = "Campaign management operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CampaignUpdateResourceImpl implements CampaignUpdateResource {

    private final CampaignUpdateDelegate delegate;

    @Inject
    public CampaignUpdateResourceImpl(@Delegate CampaignUpdateResource delegate) {
        this.delegate = (CampaignUpdateDelegate) delegate;
    }

    @PUT
    @Path("/{id}")
    @Override
    @Operation(summary = "Update a campaign", description = "Update campaign details. Only the Dungeon Master can update.")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Campaign updated successfully",
                    content = @Content(schema = @Schema(implementation = CampaignViewModel.class))
            ),
            @APIResponse(responseCode = "400", description = "Invalid request"),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "403", description = "Forbidden - not the Dungeon Master"),
            @APIResponse(responseCode = "404", description = "Campaign not found")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public CampaignViewModel update(
            @Parameter(description = "Campaign ID", required = true, example = "1")
            @PathParam("id") Long id,
            @RequestBody(
                    description = "Campaign update details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateCampaignRequest.class))
            )
            @Valid UpdateCampaignRequest request) {

        return delegate.update(id, request);
    }
}
