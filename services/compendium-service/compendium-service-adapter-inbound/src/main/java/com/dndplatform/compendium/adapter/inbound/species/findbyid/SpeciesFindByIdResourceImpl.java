package com.dndplatform.compendium.adapter.inbound.species.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.SpeciesFindByIdResource;
import com.dndplatform.compendium.view.model.vm.SpeciesViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@RequestScoped
@Path("/api/compendium/species")
@Tag(name = "Species", description = "Species/Race reference data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SpeciesFindByIdResourceImpl implements SpeciesFindByIdResource {

    private final SpeciesFindByIdResource delegate;

    @Inject
    public SpeciesFindByIdResourceImpl(@Delegate SpeciesFindByIdResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Path("/{id}")
    @Override
    @Operation(summary = "Get species by ID", description = "Retrieve a specific species by its ID")
    @APIResponse(responseCode = "200", description = "Species found")
    @APIResponse(responseCode = "404", description = "Species not found")
    public SpeciesViewModel findById(
            @Parameter(description = "Species ID", required = true, example = "1")
            @PathParam("id") int id) {
        return delegate.findById(id);
    }
}
