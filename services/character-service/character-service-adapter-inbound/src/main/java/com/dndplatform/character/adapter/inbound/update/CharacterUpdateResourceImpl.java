package com.dndplatform.character.adapter.inbound.update;

import com.dndplatform.character.view.model.CharacterUpdateResource;
import com.dndplatform.character.view.model.vm.CharacterViewModel;
import com.dndplatform.character.view.model.vm.UpdateCharacterRequest;
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
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import static org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType.HTTP;

@RequestScoped
@RunOnVirtualThread
@Path("/characters")
@Tag(name = "Character", description = "Character management operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class CharacterUpdateResourceImpl implements CharacterUpdateResource {

    private final CharacterUpdateDelegate delegate;

    @Inject
    public CharacterUpdateResourceImpl(@Delegate CharacterUpdateResource delegate) {
        this.delegate = (CharacterUpdateDelegate) delegate;
    }

    @PUT
    @Path("/{id}")
    @Override
    @Operation(summary = "Update an existing character", description = "Update a D&D 5E character with full details and recalculate derived stats")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Character updated successfully",
                    content = @Content(schema = @Schema(implementation = CharacterViewModel.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid request - validation failed"
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Unauthorized - missing or invalid token"
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "Forbidden - user does not own this character"
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Character not found or invalid compendium reference"
            )
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public CharacterViewModel update(
            @PathParam("id") Long id,
            @RequestBody(
                    description = "Character update details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateCharacterRequest.class))
            )
            @Valid UpdateCharacterRequest request) {

        return delegate.update(id, request);
    }
}
