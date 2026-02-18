package com.dndplatform.campaign.adapter.inbound.create;

import com.dndplatform.campaign.view.model.CampaignCreateResource;
import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
import com.dndplatform.campaign.view.model.vm.CreateCampaignRequest;
import com.dndplatform.common.annotations.Delegate;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import static org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType.HTTP;

@RequestScoped
@RunOnVirtualThread
@Path("/campaigns")
@Tag(name = "Campaign", description = "Campaign management operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class CampaignCreateResourceImpl implements CampaignCreateResource {

    private final CampaignCreateDelegate delegate;

    @Inject
    public CampaignCreateResourceImpl(@Delegate CampaignCreateResource delegate) {
        this.delegate = (CampaignCreateDelegate) delegate;
    }

    @POST
    @Override
    @Operation(summary = "Create a new campaign", description = "Create a new campaign. The caller becomes the Dungeon Master.")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Campaign created successfully",
                    content = @Content(schema = @Schema(implementation = CampaignViewModel.class))
            ),
            @APIResponse(responseCode = "400", description = "Invalid request - validation failed"),
            @APIResponse(responseCode = "401", description = "Unauthorized - missing or invalid token")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public CampaignViewModel create(
            @RequestBody(
                    description = "Campaign creation details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateCampaignRequest.class))
            )
            @Valid CreateCampaignRequest request) {

        return delegate.create(request);
    }
}
