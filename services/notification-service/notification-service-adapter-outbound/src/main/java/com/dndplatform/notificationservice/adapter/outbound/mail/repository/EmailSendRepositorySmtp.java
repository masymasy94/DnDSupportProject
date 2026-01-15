package com.dndplatform.notificationservice.adapter.outbound.mail.repository;

import com.dndplatform.notificationservice.domain.model.Email;
import com.dndplatform.notificationservice.domain.repository.EmailSendRepository;
import io.quarkus.mailer.reactive.ReactiveMailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class EmailSendRepositorySmtp implements EmailSendRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ReactiveMailer mailer;
    private final EmailToMailMapper emailToMailMapper;

    @Inject
    public EmailSendRepositorySmtp(ReactiveMailer mailer,
                                   EmailToMailMapper emailToMailMapper) {
        this.mailer = mailer;
        this.emailToMailMapper = emailToMailMapper;
    }

    @Override
    public void send(Email email) {
        mailer.send(emailToMailMapper.apply(email))
            .subscribe().with(
                success -> log.info("Email sent successfully to: " + email.to()),
                failure -> log.log(Level.SEVERE, "Error while sending email to: " + email.to(), failure)
            );
    }
}
