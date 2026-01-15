package com.dndplatform.notificationservice.adapter.inbound.messaging.impl;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.notificationservice.adapter.inbound.messaging.EmailSendRequestConsumer;
import com.dndplatform.notificationservice.adapter.inbound.messaging.MessageAcknowledger;
import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModel;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.annotation.Nullable;
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
public class EmailSendRequestConsumerImpl implements EmailSendRequestConsumer {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EmailSendRequestConsumer delegate;
    private final MessageAcknowledger acknowledger;

    @Inject
    public EmailSendRequestConsumerImpl(@Delegate EmailSendRequestConsumer delegate,
                                        MessageAcknowledger acknowledger) {
        this.delegate = delegate;
        this.acknowledger = acknowledger;
    }

    @WithSpan
    @Blocking
    @Incoming("email-requests-in")
    public CompletionStage<Void> consumeEmailRequest(Message<EmailSendRequestViewModel> message) {
        var request = message.getPayload();
        log.info(() -> "Received async email request for: " + request.to());

        return delegate.consumeEmailRequest(message)
                .handle((result, throwable) ->
                        acknowledgeMessage(message, throwable, request));
    }

    @Nullable
    private Void acknowledgeMessage(Message<EmailSendRequestViewModel> message, Throwable throwable, EmailSendRequestViewModel request) {
        if (throwable != null) {
            log.log(Level.SEVERE, "Failed to send email to: " + request.to(), throwable);
            acknowledger.nack(message, throwable);
        } else {
            acknowledger.ack(message);
        }
        return null;
    }
}
