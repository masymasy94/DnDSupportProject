package com.dndplatform.asset.adapter.inbound.documents.upload;

import com.dndplatform.asset.view.model.DocumentUploadResource;
import com.dndplatform.asset.view.model.vm.DocumentViewModel;
import com.dndplatform.common.annotations.Delegate;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

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
public class DocumentUploadResourceImpl implements DocumentUploadResource {

    private final DocumentUploadResource delegate;
    private final JsonWebToken jwt;

    @Inject
    public DocumentUploadResourceImpl(@Delegate DocumentUploadResource delegate, JsonWebToken jwt) {
        this.delegate = delegate;
        this.jwt = jwt;
    }

    @POST
    @Override
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Upload a document", description = "Upload a PDF document to the storage")
    @APIResponse(responseCode = "200", description = "Document uploaded successfully")
    @APIResponse(responseCode = "400", description = "Invalid file type or size")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public DocumentViewModel upload(
            @Parameter(description = "The document file to upload")
            @RestForm("file") FileUpload file,
            @Parameter(hidden = true) String uploadedBy
    ) {
        String userId = jwt.getSubject();
        return delegate.upload(file, userId);
    }
}
