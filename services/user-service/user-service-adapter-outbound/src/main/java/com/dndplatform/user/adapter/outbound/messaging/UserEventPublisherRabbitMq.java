package com.dndplatform.user.adapter.outbound.messaging;

import com.dndplatform.user.domain.event.UserEventPublisher;
import com.dndplatform.user.domain.event.UserRegisteredEvent;
import io.smallrye.reactive.messaging.rabbitmq.OutgoingRabbitMQMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Metadata;

import java.util.logging.Logger;

@ApplicationScoped
public class UserEventPublisherRabbitMq implements UserEventPublisher {

    private static final String ROUTING_KEY_USER_REGISTERED = "user.registered";
    private final Logger log = Logger.getLogger(getClass().getName());
    private final Emitter<UserRegisteredEvent> emitter;


    @Inject
    public UserEventPublisherRabbitMq(@Channel("user-events") Emitter<UserRegisteredEvent> emitter) {
        this.emitter = emitter;
    }

    @Override
    public void publishUserRegistered(UserRegisteredEvent event) {
        log.info(() -> "Publishing user registered event for: %s".formatted(event.email()));

        var metadata = Metadata.of(
                new OutgoingRabbitMQMetadata.Builder()
                        .withRoutingKey(ROUTING_KEY_USER_REGISTERED)
                        .build()
        );

        emitter.send(Message.of(event, metadata));
    }
}