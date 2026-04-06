package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.view.model.EncounterCreateResource;
import com.dndplatform.combat.view.model.vm.CreateEncounterRequest;
import com.dndplatform.combat.view.model.vm.EncounterViewModel;
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
@Path("/encounters")
@Tag(name = "Encounters", description = "Encounter management operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class EncounterCreateResourceImpl implements EncounterCreateResource {

    private final EncounterCreateDelegate delegate;

    @Inject
    public EncounterCreateResourceImpl(@Delegate EncounterCreateResource delegate) {
        this.delegate = (EncounterCreateDelegate) delegate;
    }

    @POST
    @Override
    @Operation(summary = "Create a new encounter", description = "Create a new encounter for a campaign")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Encounter created successfully",
                    content = @Content(schema = @Schema(implementation = EncounterViewModel.class))
            ),
            @APIResponse(responseCode = "400", description = "Invalid request"),
            @APIResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public EncounterViewModel create(
            @RequestBody(
                    description = "Encounter creation details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateEncounterRequest.class))
            )
            @Valid CreateEncounterRequest request) {
        return delegate.create(request);
    }
}
