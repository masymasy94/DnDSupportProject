package com.dndplatform.compendium.adapter.inbound.spells.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.SpellFindAllResource;
import io.smallrye.common.annotation.RunOnVirtualThread;
import com.dndplatform.compendium.view.model.vm.PagedSpellViewModel;
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
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType.HTTP;

@RequestScoped
@RunOnVirtualThread
@Path("/api/compendium/spells")
@Tag(name = "Spells", description = "Spell reference data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SpellFindAllResourceImpl implements SpellFindAllResource {

    private final SpellFindAllResource delegate;

    @Inject
    public SpellFindAllResourceImpl(@Delegate SpellFindAllResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Override
    @Operation(summary = "Get all spells", description = "Retrieve all spells with optional filters and pagination")
    @APIResponse(responseCode = "200", description = "Spell list retrieved successfully")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public PagedSpellViewModel findAll(
            @Parameter(description = "Search in spell name and description")
            @QueryParam("search") String search,
            @Parameter(description = "Filter by spell levels (0-9), can specify multiple",
                    schema = @Schema(type = SchemaType.ARRAY, implementation = Integer.class))
            @QueryParam("level") List<Integer> levels,
            @Parameter(description = "Filter by schools of magic, can specify multiple",
                    schema = @Schema(type = SchemaType.ARRAY, implementation = String.class))
            @QueryParam("school") List<String> schools,
            @Parameter(description = "Filter by concentration requirement")
            @QueryParam("concentration") Boolean concentration,
            @Parameter(description = "Filter by ritual casting")
            @QueryParam("ritual") Boolean ritual,

            @Parameter(description = "Page number (0-indexed)")
            @QueryParam("page") @DefaultValue("0") Integer page,

            @Parameter(description = "Page size (default 50)")
            @QueryParam("pageSize") @DefaultValue("50") Integer pageSize
    ) {
        return delegate.findAll(search, levels, schools, concentration, ritual, page, pageSize);
    }
}
