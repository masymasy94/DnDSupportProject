package com.dndplatform.notificationservice.adapter.inbound.messaging;

import com.dndplatform.notificationservice.adapter.inbound.send.mapper.SendEmailRequestMapper;
import com.dndplatform.notificationservice.domain.SendEmailService;
import com.dndplatform.notificationservice.view.model.vm.SendEmailRequestViewModel;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class EmailQueueConsumer {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final SendEmailRequestMapper requestMapper;
    private final SendEmailService sendEmailService;

    @Inject
    public EmailQueueConsumer(SendEmailRequestMapper requestMapper,
                              SendEmailService sendEmailService) {
        this.requestMapper = requestMapper;
        this.sendEmailService = sendEmailService;
    }

    @Incoming("email-requests")
    @Blocking
    public void consumeEmailRequest(SendEmailRequestViewModel request) {
        log.info(() -> "Received async email request for: " + request.to());

        try {
            var email = requestMapper.apply(request);
            var result = sendEmailService.send(email);
            log.info(() -> "Async email sent with messageId: " + result.messageId());
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to send async email to: " + request.to(), e);
            throw e;
        }
    }
}
