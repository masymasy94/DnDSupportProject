package com.dndplatform.compendium.adapter.inbound.alignments.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.AlignmentFindAllResource;
import com.dndplatform.compendium.view.model.vm.AlignmentViewModel;
import io.quarkus.cache.CacheResult;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType.HTTP;

@RequestScoped
@RunOnVirtualThread
@Path("/api/compendium/alignments")
@Tag(name = "Alignments", description = "D&D alignment reference data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class AlignmentFindAllResourceImpl implements AlignmentFindAllResource {

    private final AlignmentFindAllResource delegate;

    @Inject
    public AlignmentFindAllResourceImpl(@Delegate AlignmentFindAllResource delegate) {
        this.delegate = delegate;
    }


    @GET
    @Override
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    @Operation(summary = "Get all alignments", description = "Retrieve all D&D alignments")
    @APIResponse(responseCode = "200", description = "Alignments list retrieved successfully")
    @CacheResult(cacheName = "alignments-cache")
    public List<AlignmentViewModel> findAll() {
        return delegate.findAll();
    }
}
