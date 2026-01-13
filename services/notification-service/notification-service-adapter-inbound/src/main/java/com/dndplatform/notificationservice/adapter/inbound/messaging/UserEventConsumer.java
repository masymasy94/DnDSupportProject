package com.dndplatform.notificationservice.adapter.inbound.messaging;

import com.dndplatform.notificationservice.domain.SendEmailService;
import com.dndplatform.notificationservice.domain.model.EmailBuilder;
import com.dndplatform.notificationservice.domain.repository.FindEmailTemplateByNameRepository;
import com.dndplatform.notificationservice.view.model.vm.UserRegisteredEventViewModel;
import io.smallrye.reactive.messaging.annotations.Blocking;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class UserEventConsumer {

    private static final String WELCOME_TEMPLATE_NAME = "welcome-new-user";

    private final Logger log = Logger.getLogger(getClass().getName());
    private final SendEmailService sendEmailService;
    private final FindEmailTemplateByNameRepository findEmailTemplateByNameRepository;

    @Inject
    public UserEventConsumer(SendEmailService sendEmailService,
                             FindEmailTemplateByNameRepository findEmailTemplateByNameRepository) {
        this.sendEmailService = sendEmailService;
        this.findEmailTemplateByNameRepository = findEmailTemplateByNameRepository;
    }

    @Incoming("user-events")
    @Blocking
    @ActivateRequestContext
    public void consumeUserRegisteredEvent(JsonObject json) {
        var event = new UserRegisteredEventViewModel(
                json.getLong("userId"),
                json.getString("email"),
                json.getString("username")
        );
        log.info(() -> "Received user registered event for: " + event.email());

        try {
            var templateOptional = findEmailTemplateByNameRepository.findByName(WELCOME_TEMPLATE_NAME);

            if (templateOptional.isEmpty()) {
                log.warning(() -> "Welcome template '" + WELCOME_TEMPLATE_NAME + "' not found. Skipping welcome email.");
                return;
            }

            var template = templateOptional.get();

            var email = EmailBuilder.builder()
                    .withTo(event.email())
                    .withTemplateId(template.id())
                    .withCc(Collections.emptyList())
                    .withBcc(Collections.emptyList())
                    .withAttachments(Collections.emptyList())
                    .build();

            var result = sendEmailService.send(email);
            log.info(() -> "Welcome email sent to " + event.email() + " with messageId: " + result.messageId());
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to send welcome email to: " + event.email(), e);
        }
    }
}