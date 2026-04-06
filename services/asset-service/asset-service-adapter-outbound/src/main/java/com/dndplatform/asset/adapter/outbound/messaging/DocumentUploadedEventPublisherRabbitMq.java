package com.dndplatform.asset.adapter.outbound.messaging;

import com.dndplatform.asset.domain.event.DocumentUploadedEventPublisher;
import com.dndplatform.asset.domain.model.Document;
import com.dndplatform.asset.view.model.vm.DocumentUploadedEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.logging.Logger;

@ApplicationScoped
public class DocumentUploadedEventPublisherRabbitMq implements DocumentUploadedEventPublisher {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final Emitter<DocumentUploadedEvent> emitter;

    @Inject
    public DocumentUploadedEventPublisherRabbitMq(@Channel("document-events-out") Emitter<DocumentUploadedEvent> emitter) {
        this.emitter = emitter;
    }

    @Override
    public void publish(Document document) {
        log.info(() -> "Publishing document.uploaded event for: %s".formatted(document.id()));

        var event = new DocumentUploadedEvent(
                document.id(),
                document.fileName(),
                document.contentType(),
                document.uploadedBy()
        );
        emitter.send(event);
    }
}
