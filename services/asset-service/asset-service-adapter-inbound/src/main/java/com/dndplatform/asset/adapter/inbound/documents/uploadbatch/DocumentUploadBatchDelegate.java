package com.dndplatform.asset.adapter.inbound.documents.uploadbatch;

import com.dndplatform.asset.domain.DocumentUploadBatchService;
import com.dndplatform.asset.domain.model.DocumentContent;
import com.dndplatform.asset.view.model.DocumentUploadBatchResource;
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
public class DocumentUploadBatchDelegate implements DocumentUploadBatchResource {

    private final DocumentUploadBatchService service;

    @Inject
    public DocumentUploadBatchDelegate(DocumentUploadBatchService service) {
        this.service = service;
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
            return documents.stream()
                    .map(doc -> new DocumentViewModel(
                            doc.id(),
                            doc.fileName(),
                            doc.contentType(),
                            doc.size(),
                            doc.uploadedBy(),
                            doc.uploadedAt()
                    ))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read uploaded files", e);
        }
    }
}
