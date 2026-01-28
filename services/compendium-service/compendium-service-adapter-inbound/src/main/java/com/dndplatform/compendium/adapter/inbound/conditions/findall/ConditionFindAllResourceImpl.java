package com.dndplatform.compendium.adapter.inbound.conditions.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.ConditionFindAllResource;
import com.dndplatform.compendium.view.model.vm.ConditionViewModel;
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
public class ConditionFindAllResourceImpl implements ConditionFindAllResource {

    private final ConditionFindAllResource delegate;

    @Inject
    public ConditionFindAllResourceImpl(@Delegate ConditionFindAllResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Override
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    @Operation(summary = "Get all conditions", description = "Retrieve all D&D conditions")
    @APIResponse(responseCode = "200", description = "Conditions list retrieved successfully")
    @CacheResult(cacheName = "conditions-cache")
    public List<ConditionViewModel> findAll() {
        return delegate.findAll();
    }
}
