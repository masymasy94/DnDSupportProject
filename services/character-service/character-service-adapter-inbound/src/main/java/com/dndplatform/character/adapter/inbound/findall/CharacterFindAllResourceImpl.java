package com.dndplatform.character.adapter.inbound.findall;

import com.dndplatform.character.view.model.CharacterFindAllResource;
import com.dndplatform.character.view.model.vm.PagedCharactersViewModel;
import com.dndplatform.common.annotations.Delegate;
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
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@RequestScoped
@Path("/characters")
@Tag(name = "Character", description = "Character management operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CharacterFindAllResourceImpl implements CharacterFindAllResource {

    private final CharacterFindAllResource delegate;

    @Inject
    public CharacterFindAllResourceImpl(@Delegate CharacterFindAllResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Override
    @Operation(summary = "Get all characters", description = "Retrieve a paginated list of all characters")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Characters retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PagedCharactersViewModel.class))
            )
    })
    public PagedCharactersViewModel findAll(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @QueryParam("page") @DefaultValue("0") int page,
            @Parameter(description = "Page size", example = "20")
            @QueryParam("size") @DefaultValue("20") int size) {
        return delegate.findAll(page, size);
    }
}
