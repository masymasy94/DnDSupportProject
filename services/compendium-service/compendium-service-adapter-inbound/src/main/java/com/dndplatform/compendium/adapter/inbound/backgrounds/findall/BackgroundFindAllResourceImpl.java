package com.dndplatform.compendium.adapter.inbound.backgrounds.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.BackgroundFindAllResource;
import com.dndplatform.compendium.view.model.vm.BackgroundViewModel;
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
@Path("/api/compendium/backgrounds")
@Tag(name = "Backgrounds", description = "D&D background reference data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class BackgroundFindAllResourceImpl implements BackgroundFindAllResource {

    private final BackgroundFindAllResource delegate;

    @Inject
    public BackgroundFindAllResourceImpl(@Delegate BackgroundFindAllResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Override
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    @Operation(summary = "Get all backgrounds", description = "Retrieve all D&D backgrounds")
    @APIResponse(responseCode = "200", description = "Backgrounds list retrieved successfully")
    @CacheResult(cacheName = "backgrounds-cache")
    public List<BackgroundViewModel> findAll() {
        return delegate.findAll();
    }
}
