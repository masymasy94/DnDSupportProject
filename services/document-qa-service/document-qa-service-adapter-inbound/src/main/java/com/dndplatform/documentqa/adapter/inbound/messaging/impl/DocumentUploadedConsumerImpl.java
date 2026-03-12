package com.dndplatform.documentqa.adapter.inbound.messaging.impl;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.documentqa.adapter.inbound.messaging.DocumentUploadedConsumer;
import com.dndplatform.documentqa.view.model.vm.DocumentUploadedEventVm;
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
public class DocumentUploadedConsumerImpl implements DocumentUploadedConsumer {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final DocumentUploadedConsumer delegate;

    @Inject
    public DocumentUploadedConsumerImpl(@Delegate DocumentUploadedConsumer delegate) {
        this.delegate = delegate;
    }

    @WithSpan
    @Blocking
    @Incoming("document-uploaded-in")
    @Override
    public CompletionStage<Void> consume(Message<DocumentUploadedEventVm> message) {
        var event = message.getPayload();
        log.info(() -> "Received document.uploaded event for: %s (%s)"
                .formatted(event.documentId(), event.fileName()));

        return delegate.consume(message)
                .handle((result, throwable) -> {
                    if (throwable != null) {
                        log.log(Level.SEVERE, "Failed to process document.uploaded event: " + event.documentId(), throwable);
                        message.nack(throwable);
                    } else {
                        message.ack();
                    }
                    return null;
                });
    }
}
