package com.dndplatform.asset.adapter.inbound.documents.upload;

import com.dndplatform.asset.domain.DocumentUploadService;
import com.dndplatform.asset.domain.model.Document;
import com.dndplatform.asset.view.model.DocumentUploadResource;
import com.dndplatform.asset.view.model.vm.DocumentViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.nio.file.Files;

@Delegate
@RequestScoped
public class DocumentUploadDelegate implements DocumentUploadResource {

    private final DocumentUploadService service;

    @Inject
    public DocumentUploadDelegate(DocumentUploadService service) {
        this.service = service;
    }

    @Override
    public DocumentViewModel upload(FileUpload file, String uploadedBy) {
        try {
            var inputStream = Files.newInputStream(file.uploadedFile());
            var document = service.upload(
                    file.fileName(),
                    file.contentType(),
                    inputStream,
                    file.size(),
                    uploadedBy
            );
            return toViewModel(document);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read uploaded file", e);
        }
    }

    private DocumentViewModel toViewModel(Document document) {
        return new DocumentViewModel(
                document.id(),
                document.fileName(),
                document.contentType(),
                document.size(),
                document.uploadedBy(),
                document.uploadedAt()
        );
    }
}
