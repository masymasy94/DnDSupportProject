package com.dndplatform.compendium.adapter.inbound.skills.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.SkillFindAllResource;
import com.dndplatform.compendium.view.model.vm.SkillViewModel;
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
@Path("/api/compendium/skills")
@Tag(name = "Skills", description = "D&D skill reference data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SkillFindAllResourceImpl implements SkillFindAllResource {

    private final SkillFindAllResource delegate;

    @Inject
    public SkillFindAllResourceImpl(@Delegate SkillFindAllResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Override
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    @Operation(summary = "Get all skills", description = "Retrieve all D&D skills")
    @APIResponse(responseCode = "200", description = "Skills list retrieved successfully")
    @CacheResult(cacheName = "skills-cache")
    public List<SkillViewModel> findAll() {
        return delegate.findAll();
    }
}
