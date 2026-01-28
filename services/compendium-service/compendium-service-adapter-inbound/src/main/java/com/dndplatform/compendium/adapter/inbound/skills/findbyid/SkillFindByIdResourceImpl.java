package com.dndplatform.compendium.adapter.inbound.skills.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.SkillFindByIdResource;
import io.smallrye.common.annotation.RunOnVirtualThread;
import com.dndplatform.compendium.view.model.vm.SkillViewModel;
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
public class SkillFindByIdResourceImpl implements SkillFindByIdResource {

    private final SkillFindByIdResource delegate;

    @Inject
    public SkillFindByIdResourceImpl(@Delegate SkillFindByIdResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Path("/{id}")
    @Override
    @Operation(summary = "Get skill by ID", description = "Retrieve a specific skill by its ID")
    @APIResponse(responseCode = "200", description = "Skill found")
    @APIResponse(responseCode = "404", description = "Skill not found")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public SkillViewModel findById(
            @Parameter(description = "Skill ID", required = true, example = "1")
            @PathParam("id") int id) {
        return delegate.findById(id);
    }
}
