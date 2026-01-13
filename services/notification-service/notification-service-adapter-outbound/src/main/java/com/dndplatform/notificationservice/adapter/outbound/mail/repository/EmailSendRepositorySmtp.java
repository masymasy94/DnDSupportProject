package com.dndplatform.notificationservice.adapter.outbound.mail.repository;

import com.dndplatform.notificationservice.domain.model.Email;
import com.dndplatform.notificationservice.domain.model.EmailAttachment;
import com.dndplatform.notificationservice.domain.model.EmailResult;
import com.dndplatform.notificationservice.domain.model.EmailStatus;
import com.dndplatform.notificationservice.domain.repository.EmailSendRepository;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class EmailSendRepositorySmtp implements EmailSendRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final Mailer mailer;

    @Inject
    public EmailSendRepositorySmtp(Mailer mailer) {
        this.mailer = mailer;
    }

    @Override
    public EmailResult send(Email email) {
        String messageId = UUID.randomUUID().toString();

        try {
            Mail mail = buildMail(email);
            mailer.send(mail);
            return new EmailResult(messageId, EmailStatus.SENT, Instant.now(), null);

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to send email to: " + email.to(), e);
            return new EmailResult(messageId, EmailStatus.FAILED, Instant.now(), e.getMessage());
        }
    }




    private Mail buildMail(Email email) {
        Mail mail = getMail(email);
        if (email.attachments() != null) {
            for (EmailAttachment attachment : email.attachments()) {
                mail.addAttachment(
                        attachment.fileName(),
                        attachment.data(),
                        attachment.contentType()
                );
            }
        }

        return mail;
    }

    private static @NonNull Mail getMail(Email email) {
        Mail mail = new Mail();
        mail.setTo(java.util.List.of(email.to()));
        mail.setSubject(email.subject());

        if (email.cc() != null && !email.cc().isEmpty()) {
            mail.setCc(email.cc());
        }
        if (email.bcc() != null && !email.bcc().isEmpty()) {
            mail.setBcc(email.bcc());
        }

        if (email.textBody() != null) {
            mail.setText(email.textBody());
        }
        if (email.htmlBody() != null) {
            mail.setHtml(email.htmlBody());
        }
        return mail;
    }
}
