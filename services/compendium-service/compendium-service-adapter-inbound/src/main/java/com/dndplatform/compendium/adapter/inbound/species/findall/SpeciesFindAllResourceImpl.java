package com.dndplatform.compendium.adapter.inbound.species.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.SpeciesFindAllResource;
import com.dndplatform.compendium.view.model.vm.SpeciesViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@RequestScoped
@Path("/api/compendium/species")
@Tag(name = "Species", description = "Species/Race reference data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SpeciesFindAllResourceImpl implements SpeciesFindAllResource {

    private final SpeciesFindAllResource delegate;

    @Inject
    public SpeciesFindAllResourceImpl(@Delegate SpeciesFindAllResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Override
    @Operation(summary = "Get all species", description = "Retrieve all species/races")
    @APIResponse(responseCode = "200", description = "Species list retrieved successfully")
    public List<SpeciesViewModel> findAll() {
        return delegate.findAll();
    }
}
