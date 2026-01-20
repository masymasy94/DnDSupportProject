package com.dndplatform.compendium.adapter.inbound.monsters.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.MonsterFindAllResource;
import com.dndplatform.compendium.view.model.vm.PagedMonsterViewModel;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import static org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType.HTTP;

@RequestScoped
@Path("/api/compendium/monsters")
@Tag(name = "Monsters", description = "Monster reference data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class MonsterFindAllResourceImpl implements MonsterFindAllResource {

    private final MonsterFindAllResource delegate;

    @Inject
    public MonsterFindAllResourceImpl(@Delegate MonsterFindAllResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Override
    @Operation(summary = "Get all monsters", description = "Retrieve all monsters with optional filters and pagination")
    @APIResponse(responseCode = "200", description = "Monster list retrieved successfully")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public PagedMonsterViewModel findAll(
            @Parameter(description = "Filter by monster name (partial match)")
            @QueryParam("name") String name,

            @Parameter(description = "Filter by monster type (e.g., Dragon, Undead, Beast)")
            @QueryParam("type") String type,

            @Parameter(description = "Filter by monster size (Tiny, Small, Medium, Large, Huge, Gargantuan)")
            @QueryParam("size") String size,

            @Parameter(description = "Filter by challenge rating (e.g., 1/4, 1, 5, 17)")
            @QueryParam("challengeRating") String challengeRating,

            @Parameter(description = "Filter by alignment (e.g., chaotic evil, lawful good)")
            @QueryParam("alignment") String alignment,

            @Parameter(description = "Page number (0-indexed)")
            @QueryParam("page") @DefaultValue("0") Integer page,

            @Parameter(description = "Page size (default 50)")
            @QueryParam("pageSize") @DefaultValue("50") Integer pageSize
    ) {
        return delegate.findAll(name, type, size, challengeRating, alignment, page, pageSize);
    }
}
