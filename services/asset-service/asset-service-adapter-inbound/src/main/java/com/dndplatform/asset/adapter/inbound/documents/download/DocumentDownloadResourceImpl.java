package com.dndplatform.asset.adapter.inbound.documents.download;

import com.dndplatform.asset.view.model.DocumentDownloadResource;
import com.dndplatform.common.annotations.Delegate;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

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
public class DocumentDownloadResourceImpl implements DocumentDownloadResource {

    private final DocumentDownloadResource delegate;

    @Inject
    public DocumentDownloadResourceImpl(@Delegate DocumentDownloadResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Path("/{documentId}")
    @Override
    @Produces("application/octet-stream")
    @Operation(summary = "Download a document", description = "Download a document by its ID")
    @APIResponse(responseCode = "200", description = "Document retrieved successfully")
    @APIResponse(responseCode = "404", description = "Document not found")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public Response download(
            @Parameter(description = "The document ID")
            @PathParam("documentId") String documentId
    ) {
        return delegate.download(documentId);
    }
}
