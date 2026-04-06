package com.dndplatform.campaign.adapter.inbound.note;

import com.dndplatform.campaign.view.model.CampaignNoteUpdateResource;
import com.dndplatform.campaign.view.model.vm.CampaignNoteViewModel;
import com.dndplatform.campaign.view.model.vm.UpdateNoteRequest;
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
@Path("/campaigns/{campaignId}/notes")
@Tag(name = "Campaign Notes", description = "Campaign note operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CampaignNoteUpdateResourceImpl implements CampaignNoteUpdateResource {

    private final CampaignNoteUpdateDelegate delegate;

    @Inject
    public CampaignNoteUpdateResourceImpl(@Delegate CampaignNoteUpdateResource delegate) {
        this.delegate = (CampaignNoteUpdateDelegate) delegate;
    }

    @PUT
    @Path("/{noteId}")
    @Override
    @Operation(summary = "Update a campaign note", description = "Update own note")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Note updated successfully",
                    content = @Content(schema = @Schema(implementation = CampaignNoteViewModel.class))
            ),
            @APIResponse(responseCode = "400", description = "Invalid request"),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "403", description = "Forbidden - not the author"),
            @APIResponse(responseCode = "404", description = "Note not found")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public CampaignNoteViewModel update(
            @Parameter(description = "Campaign ID", required = true)
            @PathParam("campaignId") Long campaignId,
            @Parameter(description = "Note ID", required = true)
            @PathParam("noteId") Long noteId,
            @RequestBody(
                    description = "Note update details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateNoteRequest.class))
            )
            @Valid UpdateNoteRequest request) {

        return delegate.update(campaignId, noteId, request);
    }
}
