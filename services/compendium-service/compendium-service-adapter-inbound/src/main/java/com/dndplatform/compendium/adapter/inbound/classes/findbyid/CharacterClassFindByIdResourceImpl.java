package com.dndplatform.compendium.adapter.inbound.classes.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.view.model.CharacterClassFindByIdResource;
import io.smallrye.common.annotation.RunOnVirtualThread;
import com.dndplatform.compendium.view.model.vm.CharacterClassViewModel;
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
@Path("/api/compendium/classes")
@Tag(name = "Classes", description = "Character class reference data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class CharacterClassFindByIdResourceImpl implements CharacterClassFindByIdResource {

    private final CharacterClassFindByIdResource delegate;

    @Inject
    public CharacterClassFindByIdResourceImpl(@Delegate CharacterClassFindByIdResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Path("/{id}")
    @Override
    @Operation(summary = "Get class by ID", description = "Retrieve a specific character class by its ID")
    @APIResponse(responseCode = "200", description = "Class found")
    @APIResponse(responseCode = "404", description = "Class not found")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public CharacterClassViewModel findById(
            @Parameter(description = "Class ID", required = true, example = "1")
            @PathParam("id") int id) {
        return delegate.findById(id);
    }
}
