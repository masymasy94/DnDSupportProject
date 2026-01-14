package com.dndplatform.user.adapter.outbound.messaging;

import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModel;
import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModelBuilder;
import com.dndplatform.user.domain.event.EmailSendRepository;
import com.dndplatform.user.domain.event.UserRegisteredEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jspecify.annotations.NonNull;

import java.util.logging.Logger;

@ApplicationScoped
public class EmailSendRepositoryRabbitMq implements EmailSendRepository {

    private static final Long WELCOME_TEMPLATE_ID = 1L;
    private final Logger log = Logger.getLogger(getClass().getName());
    private final Emitter<EmailSendRequestViewModel> emitter;

    @Inject
    public EmailSendRepositoryRabbitMq(@Channel("email-requests-out") Emitter<EmailSendRequestViewModel> emitter) {
        this.emitter = emitter;
    }

    @Override
    public void sendEmail(UserRegisteredEvent event) {
        log.info(() -> "Publishing welcome email request for: %s".formatted(event.email()));

        var payload = getPayload(event);
        emitter.send(payload);
    }

    private static @NonNull EmailSendRequestViewModel getPayload(UserRegisteredEvent event) {
        return EmailSendRequestViewModelBuilder
                .builder()
                .withTo(event.email())
                .withTemplateId(WELCOME_TEMPLATE_ID)
                .build(); // todo -mapper
    }
}