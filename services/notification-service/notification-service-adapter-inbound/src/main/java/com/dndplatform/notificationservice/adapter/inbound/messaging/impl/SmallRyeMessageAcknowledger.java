package com.dndplatform.notificationservice.adapter.inbound.messaging.impl;

import com.dndplatform.notificationservice.adapter.inbound.messaging.MessageAcknowledger;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.concurrent.CompletionStage;

/**
 * SmallRye Reactive Messaging implementation of {@link MessageAcknowledger}.
 * Request-scoped bean that wraps a {@link Message} and delegates acknowledgment operations to it.
 */
@ApplicationScoped
public class SmallRyeMessageAcknowledger implements MessageAcknowledger {


    @Override
    public CompletionStage<Void> ack(Message<?>message) {
        if (message == null) {
            throw new IllegalStateException("Message not set. Call setMessage() before ack()");
        }
        return message.ack();
    }

    @Override
    public CompletionStage<Void> nack(Message<?>message, Throwable reason) {
        if (message == null) {
            throw new IllegalStateException("Message not set. Call setMessage() before nack()");
        }
        return message.nack(reason);
    }
}
