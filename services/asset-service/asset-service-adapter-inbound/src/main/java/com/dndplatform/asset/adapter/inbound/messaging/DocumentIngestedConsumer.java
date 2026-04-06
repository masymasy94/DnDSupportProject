package com.dndplatform.asset.adapter.inbound.messaging;

import com.dndplatform.asset.domain.model.RagStatus;
import com.dndplatform.asset.domain.repository.DocumentMetadataUpdateRagStatusRepository;
import com.dndplatform.asset.view.model.vm.DocumentIngestedEvent;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.concurrent.CompletionStage;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@ActivateRequestContext
public class DocumentIngestedConsumer {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final DocumentMetadataUpdateRagStatusRepository repository;

    @Inject
    public DocumentIngestedConsumer(DocumentMetadataUpdateRagStatusRepository repository) {
        this.repository = repository;
    }

    @WithSpan
    @Blocking
    @Incoming("document-ingested-in")
    public CompletionStage<Void> consume(Message<DocumentIngestedEvent> message) {
        var event = message.getPayload();
        log.info(() -> "Received document.ingested event for: %s with status: %s"
                .formatted(event.documentId(), event.status()));

        try {
            RagStatus status = RagStatus.valueOf(event.status());
            repository.updateRagStatus(event.documentId(), status, event.errorMessage());
            return message.ack();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to process document.ingested event for: " + event.documentId(), e);
            return message.nack(e);
        }
    }
}
