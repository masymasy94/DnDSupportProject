package com.dndplatform.campaign.adapter.inbound.note;

import com.dndplatform.campaign.view.model.CampaignNoteCreateResource;
import com.dndplatform.campaign.view.model.vm.CampaignNoteViewModel;
import com.dndplatform.campaign.view.model.vm.CreateNoteRequest;
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
public class CampaignNoteCreateResourceImpl implements CampaignNoteCreateResource {

    private final CampaignNoteCreateDelegate delegate;

    @Inject
    public CampaignNoteCreateResourceImpl(@Delegate CampaignNoteCreateResource delegate) {
        this.delegate = (CampaignNoteCreateDelegate) delegate;
    }

    @POST
    @Override
    @Operation(summary = "Create a campaign note", description = "Create a PUBLIC or PRIVATE note in the campaign")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Note created successfully",
                    content = @Content(schema = @Schema(implementation = CampaignNoteViewModel.class))
            ),
            @APIResponse(responseCode = "400", description = "Invalid request"),
            @APIResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public CampaignNoteViewModel create(
            @Parameter(description = "Campaign ID", required = true)
            @PathParam("campaignId") Long campaignId,
            @RequestBody(
                    description = "Note details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateNoteRequest.class))
            )
            @Valid CreateNoteRequest request) {

        return delegate.create(campaignId, request);
    }
}
