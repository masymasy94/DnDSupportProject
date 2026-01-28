package com.dndplatform.asset.adapter.inbound.documents.list;

import com.dndplatform.asset.view.model.DocumentListResource;
import com.dndplatform.asset.view.model.vm.DocumentListItemViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType.HTTP;

@RequestScoped
@Path("/api/assets/documents")
@Tag(name = "Documents", description = "Document upload and retrieval")
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class DocumentListResourceImpl implements DocumentListResource {

    private final DocumentListResource delegate;

    @Inject
    public DocumentListResourceImpl(@Delegate DocumentListResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "List all documents", description = "Get a list of all available documents with their IDs and names")
    @APIResponse(responseCode = "200", description = "Document list retrieved successfully")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public List<DocumentListItemViewModel> listAll() {
        return delegate.listAll();
    }
}
