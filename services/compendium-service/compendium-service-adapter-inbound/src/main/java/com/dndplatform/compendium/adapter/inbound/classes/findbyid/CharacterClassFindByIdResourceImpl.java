package com.dndplatform.compendium.adapter.inbound.classes.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.CharacterClassFindByIdResource;
import com.dndplatform.compendium.view.model.vm.CharacterClassViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@RequestScoped
@Path("/api/compendium/classes")
@Tag(name = "Classes", description = "Character class reference data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CharacterClassFindByIdResourceImpl implements CharacterClassFindByIdResource {

    private final CharacterClassFindByIdResource delegate;

    @Inject
    public CharacterClassFindByIdResourceImpl(@Delegate CharacterClassFindByIdResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Path("/{id}")
    @Override
    @Operation(summary = "Get class by ID", description = "Retrieve a specific character class by its ID")
    @APIResponse(responseCode = "200", description = "Class found")
    @APIResponse(responseCode = "404", description = "Class not found")
    public CharacterClassViewModel findById(
            @Parameter(description = "Class ID", required = true, example = "1")
            @PathParam("id") int id) {
        return delegate.findById(id);
    }
}
