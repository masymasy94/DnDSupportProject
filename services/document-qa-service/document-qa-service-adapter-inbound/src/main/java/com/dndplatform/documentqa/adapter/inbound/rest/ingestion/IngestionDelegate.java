package com.dndplatform.documentqa.adapter.inbound.rest.ingestion;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.documentqa.domain.DocumentIngestionService;
import com.dndplatform.documentqa.domain.IngestionStatusService;
import com.dndplatform.documentqa.domain.model.DocumentIngestion;
import com.dndplatform.documentqa.domain.model.DocumentIngestionEventBuilder;
import com.dndplatform.documentqa.view.model.IngestionResource;
import com.dndplatform.documentqa.view.model.vm.IngestionStatusViewModel;
import com.dndplatform.documentqa.view.model.vm.IngestionStatusViewModelBuilder;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class IngestionDelegate implements IngestionResource {

    private final IngestionStatusService statusService;
    private final DocumentIngestionService ingestionService;

    @Inject
    public IngestionDelegate(IngestionStatusService statusService,
                             DocumentIngestionService ingestionService) {
        this.statusService = statusService;
        this.ingestionService = ingestionService;
    }

    @Override
    public IngestionStatusViewModel getStatus(String documentId) {
        DocumentIngestion ingestion = statusService.getStatus(documentId);
        return IngestionStatusViewModelBuilder.builder()
                .withDocumentId(ingestion.documentId())
                .withFileName(ingestion.fileName())
                .withStatus(ingestion.status().name())
                .withChunkCount(ingestion.chunkCount())
                .withStartedAt(ingestion.startedAt())
                .withCompletedAt(ingestion.completedAt())
                .withErrorMessage(ingestion.errorMessage())
                .build();
    }

    @Override
    public void triggerIngestion(String documentId, Long userId) {
        DocumentIngestion existing = statusService.getStatus(documentId);
        var event = DocumentIngestionEventBuilder.builder()
                .withDocumentId(documentId)
                .withFileName(existing.fileName())
                .withContentType(existing.contentType())
                .withUploadedBy(userId != null ? userId.toString() : null)
                .build();
        ingestionService.ingest(event);
    }
}
