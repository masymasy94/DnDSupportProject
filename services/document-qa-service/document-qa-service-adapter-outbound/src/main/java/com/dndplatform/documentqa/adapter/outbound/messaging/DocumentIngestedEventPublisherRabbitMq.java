package com.dndplatform.documentqa.adapter.outbound.messaging;

import com.dndplatform.documentqa.domain.event.DocumentIngestedEventPublisher;
import com.dndplatform.documentqa.domain.model.IngestionStatus;
import com.dndplatform.documentqa.view.model.vm.DocumentIngestedEventVm;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.logging.Logger;

@ApplicationScoped
public class DocumentIngestedEventPublisherRabbitMq implements DocumentIngestedEventPublisher {

    private final Logger log = Logger.getLogger(getClass().getName());

    @Inject
    @Channel("document-ingested-out")
    Emitter<DocumentIngestedEventVm> emitter;

    @Override
    public void publish(String documentId, IngestionStatus status, String errorMessage, Integer chunkCount) {
        String statusString = switch (status) {
            case COMPLETED -> "COMPLETED";
            case FAILED -> "FAILED";
            default -> status.name();
        };

        DocumentIngestedEventVm event = new DocumentIngestedEventVm(
                documentId,
                statusString,
                errorMessage,
                chunkCount
        );

        emitter.send(event);
        log.info(() -> "Published document ingested event: documentId=%s, status=%s".formatted(documentId, statusString));
    }
}
