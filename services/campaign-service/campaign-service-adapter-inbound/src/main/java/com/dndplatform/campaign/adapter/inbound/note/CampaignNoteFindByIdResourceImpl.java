package com.dndplatform.campaign.adapter.inbound.note;

import com.dndplatform.campaign.view.model.CampaignNoteFindByIdResource;
import com.dndplatform.campaign.view.model.vm.CampaignNoteViewModel;
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
@Path("/campaigns/{campaignId}/notes")
@Tag(name = "Campaign Notes", description = "Campaign note operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CampaignNoteFindByIdResourceImpl implements CampaignNoteFindByIdResource {

    private final CampaignNoteFindByIdDelegate delegate;

    @Inject
    public CampaignNoteFindByIdResourceImpl(@Delegate CampaignNoteFindByIdResource delegate) {
        this.delegate = (CampaignNoteFindByIdDelegate) delegate;
    }

    @GET
    @Path("/{noteId}")
    @Override
    @Operation(summary = "Get a campaign note", description = "Get a single note if visible to the caller")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Note retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CampaignNoteViewModel.class))
            ),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "403", description = "Forbidden - private note not owned by caller"),
            @APIResponse(responseCode = "404", description = "Note not found")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public CampaignNoteViewModel findById(
            @Parameter(description = "Campaign ID", required = true)
            @PathParam("campaignId") Long campaignId,
            @Parameter(description = "Note ID", required = true)
            @PathParam("noteId") Long noteId,
            @Parameter(description = "User ID", required = true)
            @QueryParam("userId") Long userId) {

        return delegate.findById(campaignId, noteId, userId);
    }
}
