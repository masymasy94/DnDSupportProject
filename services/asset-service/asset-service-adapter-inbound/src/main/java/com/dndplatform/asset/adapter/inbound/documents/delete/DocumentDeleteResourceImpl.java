package com.dndplatform.asset.adapter.inbound.documents.delete;

import com.dndplatform.asset.view.model.DocumentDeleteResource;
import com.dndplatform.common.annotations.Delegate;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import static org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType.HTTP;

@RequestScoped
@RunOnVirtualThread
@Path("/api/assets/documents")
@Tag(name = "Documents", description = "Document upload and retrieval")
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class DocumentDeleteResourceImpl implements DocumentDeleteResource {

    private final DocumentDeleteResource delegate;

    @Inject
    public DocumentDeleteResourceImpl(@Delegate DocumentDeleteResource delegate) {
        this.delegate = delegate;
    }

    @DELETE
    @Path("/{documentId}")
    @Override
    @Operation(summary = "Delete a document", description = "Delete a document and its metadata by ID")
    @APIResponse(responseCode = "204", description = "Document deleted successfully")
    @APIResponse(responseCode = "404", description = "Document not found")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public Response delete(
            @Parameter(description = "The document ID")
            @PathParam("documentId") String documentId
    ) {
        return delegate.delete(documentId);
    }
}
