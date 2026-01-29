package com.dndplatform.asset.adapter.inbound.documents.upload;

import com.dndplatform.asset.domain.DocumentUploadService;
import com.dndplatform.asset.domain.model.Document;
import com.dndplatform.asset.domain.model.DocumentContent;
import com.dndplatform.asset.view.model.DocumentUploadResource;
import com.dndplatform.asset.view.model.vm.DocumentViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<DocumentViewModel> uploadMultiple(List<FileUpload> files, String uploadedBy) {
        try {
            List<DocumentContent> documentContents = new ArrayList<>();
            for (FileUpload file : files) {
                var inputStream = Files.newInputStream(file.uploadedFile());
                documentContents.add(new DocumentContent(
                        file.fileName(),
                        file.contentType(),
                        file.size(),
                        inputStream
                ));
            }
            var documents = service.uploadMultiple(documentContents, uploadedBy);
            return documents.stream().map(this::toViewModel).toList();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read uploaded files", e);
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
