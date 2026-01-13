package com.dndplatform.notificationservice.adapter.outbound.mail.repository;

import com.dndplatform.notificationservice.domain.model.Email;
import com.dndplatform.notificationservice.domain.model.EmailResult;
import com.dndplatform.notificationservice.domain.model.EmailStatus;
import com.dndplatform.notificationservice.domain.repository.EmailSendRepository;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class EmailSendRepositorySmtp implements EmailSendRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final Mailer mailer;
    private final EmailToMailMapper emailToMailMapper;

    @Inject
    public EmailSendRepositorySmtp(Mailer mailer, EmailToMailMapper emailToMailMapper) {
        this.mailer = mailer;
        this.emailToMailMapper = emailToMailMapper;
    }

    @Override
    public EmailResult send(Email email) {
        String messageId = UUID.randomUUID().toString();
        String errorMessage = null;
        EmailStatus status = EmailStatus.SENT;

        try {
            mailer.send(emailToMailMapper.apply(email));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to send email to: " + email.to(), e);
            errorMessage = e.getMessage();
            status = EmailStatus.FAILED;
        }

        return new EmailResult(messageId, status, Instant.now(), errorMessage);
    }
}
