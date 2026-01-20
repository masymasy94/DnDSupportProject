package com.dndplatform.compendium.adapter.inbound.magicitems.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.MagicItemFindAllResource;
import com.dndplatform.compendium.view.model.vm.PagedMagicItemViewModel;
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
@Path("/api/compendium/magic-items")
@Tag(name = "Magic Items", description = "Magic item reference data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class MagicItemFindAllResourceImpl implements MagicItemFindAllResource {

    private final MagicItemFindAllResource delegate;

    @Inject
    public MagicItemFindAllResourceImpl(@Delegate MagicItemFindAllResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Override
    @Operation(summary = "Get all magic items", description = "Retrieve all magic items with optional filters and pagination")
    @APIResponse(responseCode = "200", description = "Magic item list retrieved successfully")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public PagedMagicItemViewModel findAll(
            @Parameter(description = "Filter by item name (partial match)")
            @QueryParam("name") String name,

            @Parameter(description = "Filter by rarity (Common, Uncommon, Rare, Very Rare, Legendary, Artifact)")
            @QueryParam("rarity") String rarity,

            @Parameter(description = "Filter by item type (Armor, Potion, Ring, Rod, Scroll, Staff, Wand, Weapon, Wondrous Item)")
            @QueryParam("type") String type,

            @Parameter(description = "Filter by attunement requirement (true/false)")
            @QueryParam("requiresAttunement") Boolean requiresAttunement,

            @Parameter(description = "Page number (0-indexed)")
            @QueryParam("page") @DefaultValue("0") Integer page,

            @Parameter(description = "Page size (default 50)")
            @QueryParam("pageSize") @DefaultValue("50") Integer pageSize
    ) {
        return delegate.findAll(name, rarity, type, requiresAttunement, page, pageSize);
    }
}
