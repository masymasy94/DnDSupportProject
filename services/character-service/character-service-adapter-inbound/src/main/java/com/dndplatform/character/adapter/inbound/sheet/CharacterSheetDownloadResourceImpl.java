package com.dndplatform.character.adapter.inbound.sheet;

import com.dndplatform.character.view.model.CharacterSheetDownloadResource;
import com.dndplatform.common.annotations.Delegate;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import static org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType.HTTP;

@RequestScoped
@RunOnVirtualThread
@Path("/characters/{id}/sheet")
@Tag(name = "Character", description = "Character management operations")
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class CharacterSheetDownloadResourceImpl implements CharacterSheetDownloadResource {

    private final CharacterSheetDownloadResource delegate;

    @Inject
    public CharacterSheetDownloadResourceImpl(@Delegate CharacterSheetDownloadResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Override
    @Produces("application/pdf")
    @Operation(summary = "Download character sheet PDF",
            description = "Download the generated PDF character sheet for a character")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "PDF character sheet"),
            @APIResponse(responseCode = "404", description = "Character sheet not found")
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public Response downloadSheet(@PathParam("id") Long characterId) {
        return delegate.downloadSheet(characterId);
    }
}
