package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.view.model.DifficultyCalculateResource;
import com.dndplatform.combat.view.model.vm.DifficultyResultViewModel;
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
@Path("/encounters")
@Tag(name = "Encounters", description = "Encounter management operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DifficultyCalculateResourceImpl implements DifficultyCalculateResource {

    private final DifficultyCalculateDelegate delegate;

    @Inject
    public DifficultyCalculateResourceImpl(@Delegate DifficultyCalculateResource delegate) {
        this.delegate = (DifficultyCalculateDelegate) delegate;
    }

    @GET
    @Path("/{id}/difficulty")
    @Override
    @Operation(summary = "Calculate difficulty", description = "Calculate the difficulty rating of an encounter")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Difficulty calculated successfully",
                    content = @Content(schema = @Schema(implementation = DifficultyResultViewModel.class))
            ),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "404", description = "Encounter not found")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public DifficultyResultViewModel calculate(
            @Parameter(description = "Encounter ID", required = true, example = "1")
            @PathParam("id") Long encounterId) {
        return delegate.calculate(encounterId);
    }
}
