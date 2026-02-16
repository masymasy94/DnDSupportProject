package com.dndplatform.character.adapter.inbound.create;

import com.dndplatform.character.view.model.CharacterCreateResource;
import com.dndplatform.character.view.model.vm.CharacterViewModel;
import com.dndplatform.character.view.model.vm.CreateCharacterRequest;
import com.dndplatform.common.annotations.Delegate;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;
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
public class CharacterCreateResourceImpl implements CharacterCreateResource {

    private final CharacterCreateDelegate delegate;
    private final JsonWebToken jwt;

    @Inject
    public CharacterCreateResourceImpl(@Delegate CharacterCreateResource delegate, JsonWebToken jwt) {
        this.delegate = (CharacterCreateDelegate) delegate;
        this.jwt = jwt;
    }

    @POST
    @Override
    @Operation(summary = "Create a new character", description = "Create a new D&D 5E character with full details")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Character created successfully",
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
                    responseCode = "404",
                    description = "Invalid compendium reference (species, class, background, or alignment not found)"
            )
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public CharacterViewModel create(
            @RequestBody(
                    description = "Character creation details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateCharacterRequest.class))
            )
            CreateCharacterRequest request) {

        Long userId = Long.parseLong(jwt.getSubject());
        return delegate.createWithUserId(request, userId);
    }
}
