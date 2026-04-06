package com.dndplatform.asset.adapter.inbound.documents.uploadbatch;

import com.dndplatform.asset.view.model.DocumentUploadBatchResource;
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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType.HTTP;

@RequestScoped
@RunOnVirtualThread
@Path("/api/assets/documents/batch")
@Tag(name = "Documents", description = "Document upload and retrieval")
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class DocumentUploadBatchResourceImpl implements DocumentUploadBatchResource {

    private final DocumentUploadBatchResource delegate;

    @Inject
    public DocumentUploadBatchResourceImpl(@Delegate DocumentUploadBatchResource delegate) {
        this.delegate = delegate;
    }

    @POST
    @Override
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Upload multiple documents", description = "Upload multiple documents to the storage in a single request")
    @APIResponse(responseCode = "200", description = "Documents uploaded successfully")
    @APIResponse(responseCode = "400", description = "Invalid file type or size")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public List<DocumentViewModel> uploadMultiple(
            @Parameter(description = "The document files to upload")
            @RestForm("files") List<FileUpload> files,
            @RestForm("userId") String uploadedBy
    ) {
        return delegate.uploadMultiple(files, uploadedBy);
    }
}
