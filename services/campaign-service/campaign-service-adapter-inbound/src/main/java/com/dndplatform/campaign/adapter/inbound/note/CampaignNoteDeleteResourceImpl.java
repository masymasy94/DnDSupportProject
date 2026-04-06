package com.dndplatform.campaign.adapter.inbound.note;

import com.dndplatform.campaign.view.model.CampaignNoteDeleteResource;
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
@Path("/campaigns/{campaignId}/notes")
@Tag(name = "Campaign Notes", description = "Campaign note operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CampaignNoteDeleteResourceImpl implements CampaignNoteDeleteResource {

    private final CampaignNoteDeleteDelegate delegate;

    @Inject
    public CampaignNoteDeleteResourceImpl(@Delegate CampaignNoteDeleteResource delegate) {
        this.delegate = (CampaignNoteDeleteDelegate) delegate;
    }

    @DELETE
    @Path("/{noteId}")
    @Override
    @Operation(summary = "Delete a campaign note", description = "Delete own note or DM can delete any note")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Note deleted successfully"),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "403", description = "Forbidden"),
            @APIResponse(responseCode = "404", description = "Note not found")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public void delete(
            @Parameter(description = "Campaign ID", required = true)
            @PathParam("campaignId") Long campaignId,
            @Parameter(description = "Note ID", required = true)
            @PathParam("noteId") Long noteId,
            @Parameter(description = "User ID", required = true)
            @QueryParam("userId") Long userId) {

        delegate.delete(campaignId, noteId, userId);
    }
}
