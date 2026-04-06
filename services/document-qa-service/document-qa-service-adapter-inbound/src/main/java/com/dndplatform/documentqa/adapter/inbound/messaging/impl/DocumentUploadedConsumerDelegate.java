package com.dndplatform.documentqa.adapter.inbound.messaging.impl;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.documentqa.adapter.inbound.messaging.DocumentUploadedConsumer;
import com.dndplatform.documentqa.domain.DocumentIngestionService;
import com.dndplatform.documentqa.domain.model.DocumentIngestionEventBuilder;
import com.dndplatform.documentqa.view.model.vm.DocumentUploadedEventVm;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.Objects.requireNonNull;

@Delegate
@RequestScoped
public class DocumentUploadedConsumerDelegate implements DocumentUploadedConsumer {

    private final DocumentIngestionService ingestionService;

    @Inject
    public DocumentUploadedConsumerDelegate(DocumentIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @Override
    public CompletionStage<Void> consume(Message<DocumentUploadedEventVm> message) {
        var payload = message.getPayload();
        requireNonNull(payload, "payload of DocumentUploadedEventVm is null");

        var event = DocumentIngestionEventBuilder.builder()
                .withDocumentId(payload.documentId())
                .withFileName(payload.fileName())
                .withContentType(payload.contentType())
                .withUploadedBy(payload.uploadedBy())
                .build();

        ingestionService.ingest(event);
        return CompletableFuture.completedFuture(null);
    }
}
