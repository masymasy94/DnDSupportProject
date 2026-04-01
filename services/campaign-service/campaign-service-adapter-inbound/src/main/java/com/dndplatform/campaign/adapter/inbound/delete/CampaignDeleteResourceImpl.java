package com.dndplatform.campaign.adapter.inbound.delete;

import com.dndplatform.campaign.view.model.CampaignDeleteResource;
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
@Path("/campaigns")
@Tag(name = "Campaign", description = "Campaign management operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CampaignDeleteResourceImpl implements CampaignDeleteResource {

    private final CampaignDeleteDelegate delegate;

    @Inject
    public CampaignDeleteResourceImpl(@Delegate CampaignDeleteResource delegate) {
        this.delegate = (CampaignDeleteDelegate) delegate;
    }

    @DELETE
    @Path("/{id}")
    @Override
    @Operation(summary = "Delete a campaign", description = "Delete a campaign. Only the Dungeon Master can delete.")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Campaign deleted successfully"),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "403", description = "Forbidden - not the Dungeon Master"),
            @APIResponse(responseCode = "404", description = "Campaign not found")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public void delete(
            @Parameter(description = "Campaign ID", required = true, example = "1")
            @PathParam("id") Long id,
            @Parameter(description = "User ID", required = true)
            @QueryParam("userId") Long userId) {

        delegate.delete(id, userId);
    }
}
