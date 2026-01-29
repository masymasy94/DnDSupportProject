package com.dndplatform.compendium.adapter.inbound.alignments.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.AlignmentFindByIdResource;
import io.smallrye.common.annotation.RunOnVirtualThread;
import com.dndplatform.compendium.view.model.vm.AlignmentViewModel;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import static org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType.HTTP;

@RequestScoped
@RunOnVirtualThread
@Path("/api/compendium/alignments")
@Tag(name = "Alignments", description = "Alignment reference data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class AlignmentFindByIdResourceImpl implements AlignmentFindByIdResource {

    private final AlignmentFindByIdResource delegate;

    @Inject
    public AlignmentFindByIdResourceImpl(@Delegate AlignmentFindByIdResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Path("/{id}")
    @Override
    @Operation(summary = "Get alignment by ID", description = "Retrieve a specific alignment by its ID")
    @APIResponse(responseCode = "200", description = "Alignment found")
    @APIResponse(responseCode = "404", description = "Alignment not found")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public AlignmentViewModel findById(
            @Parameter(description = "Alignment ID", required = true, example = "1")
            @PathParam("id") int id) {
        return delegate.findById(id);
    }
}
