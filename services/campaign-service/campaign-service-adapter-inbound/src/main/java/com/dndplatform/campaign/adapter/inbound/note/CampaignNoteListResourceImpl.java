package com.dndplatform.campaign.adapter.inbound.note;

import com.dndplatform.campaign.view.model.CampaignNoteListResource;
import com.dndplatform.campaign.view.model.vm.CampaignNoteViewModel;
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
@Path("/campaigns/{campaignId}/notes")
@Tag(name = "Campaign Notes", description = "Campaign note operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CampaignNoteListResourceImpl implements CampaignNoteListResource {

    private final CampaignNoteListDelegate delegate;

    @Inject
    public CampaignNoteListResourceImpl(@Delegate CampaignNoteListResource delegate) {
        this.delegate = (CampaignNoteListDelegate) delegate;
    }

    @GET
    @Override
    @Operation(summary = "List campaign notes", description = "List notes visible to the caller (own PRIVATE + all PUBLIC)")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Notes retrieved successfully"),
            @APIResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public List<CampaignNoteViewModel> listNotes(
            @Parameter(description = "Campaign ID", required = true)
            @PathParam("campaignId") Long campaignId,
            @Parameter(description = "User ID", required = true)
            @QueryParam("userId") Long userId) {

        return delegate.listNotes(campaignId, userId);
    }
}
