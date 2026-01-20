package com.dndplatform.compendium.adapter.inbound.conditions.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.ConditionFindByIdResource;
import com.dndplatform.compendium.view.model.vm.ConditionViewModel;
import io.quarkus.cache.CacheResult;
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
@Path("/api/compendium/conditions")
@Tag(name = "Conditions", description = "D&D condition reference data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class ConditionFindByIdResourceImpl implements ConditionFindByIdResource {

    private final ConditionFindByIdResource delegate;

    @Inject
    public ConditionFindByIdResourceImpl(@Delegate ConditionFindByIdResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Path("/{id}")
    @Override
    @Operation(summary = "Get condition by ID", description = "Retrieve a specific condition by its ID")
    @APIResponse(responseCode = "200", description = "Condition found")
    @APIResponse(responseCode = "404", description = "Condition not found")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    @CacheResult(cacheName = "condition-by-id-cache")
    public ConditionViewModel findById(
            @Parameter(description = "Condition ID", required = true, example = "1")
            @PathParam("id") int id) {
        return delegate.findById(id);
    }
}
