package com.dndplatform.notificationservice.adapter.inbound.messaging.impl;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.notificationservice.adapter.inbound.messaging.EmailSendRequestConsumer;
import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModel;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@ActivateRequestContext
public class EmailSendRequestConsumerImpl implements EmailSendRequestConsumer {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final EmailSendRequestConsumer delegate;

    @Inject
    public EmailSendRequestConsumerImpl(@Delegate EmailSendRequestConsumer delegate) {
        this.delegate = delegate;
    }

    @Blocking
    @Incoming("email-requests-in")
    @Override
    @WithSpan
    public void consumeEmailRequest(EmailSendRequestViewModel request) {
        log.info(() -> "Received async email request for: " + request.to());

        // todo - create messageAcknowledger ack/nack

        try {
            delegate.consumeEmailRequest(request);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to send async email to: " + request.to(), e);
        }
    }
}
