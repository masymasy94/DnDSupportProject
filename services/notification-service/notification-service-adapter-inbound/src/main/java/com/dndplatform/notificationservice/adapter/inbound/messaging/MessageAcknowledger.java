package com.dndplatform.notificationservice.adapter.inbound.messaging;

import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.concurrent.CompletionStage;

/**
 * Abstraction for message acknowledgment operations.
 * Allows consumers to acknowledge or negatively acknowledge messages
 * without coupling to specific messaging implementation.
 */
public interface MessageAcknowledger {


    /**
     * Acknowledges the message, indicating successful processing.
     *
     * @return a CompletionStage that completes when the acknowledgment is done
     */
    CompletionStage<Void> ack(Message<?> message);

    /**
     * Negatively acknowledges the message, indicating processing failure.
     * The message will be routed to the Dead Letter Queue if configured.
     *
     * @param reason the cause of the failure
     * @return a CompletionStage that completes when the nack is done
     */
    CompletionStage<Void> nack(Message<?> message, Throwable reason);
}
