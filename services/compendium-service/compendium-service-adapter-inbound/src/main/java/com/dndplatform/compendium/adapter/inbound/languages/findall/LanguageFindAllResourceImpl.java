package com.dndplatform.compendium.adapter.inbound.languages.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.LanguageFindAllResource;
import com.dndplatform.compendium.view.model.vm.LanguageViewModel;
import io.quarkus.cache.CacheResult;
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
@Path("/api/compendium/languages")
@Tag(name = "Languages", description = "D&D language reference data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class LanguageFindAllResourceImpl implements LanguageFindAllResource {

    private final LanguageFindAllResource delegate;

    @Inject
    public LanguageFindAllResourceImpl(@Delegate LanguageFindAllResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Override
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    @Operation(summary = "Get all languages", description = "Retrieve all D&D languages")
    @APIResponse(responseCode = "200", description = "Languages list retrieved successfully")
    @CacheResult(cacheName = "languages-cache")
    public List<LanguageViewModel> findAll() {
        return delegate.findAll();
    }
}
