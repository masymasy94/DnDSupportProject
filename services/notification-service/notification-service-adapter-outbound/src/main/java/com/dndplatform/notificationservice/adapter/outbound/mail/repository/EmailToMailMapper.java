package com.dndplatform.notificationservice.adapter.outbound.mail.repository;

import com.dndplatform.notificationservice.domain.model.Email;
import com.dndplatform.notificationservice.domain.model.EmailAttachment;
import io.quarkus.mailer.Mail;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.function.Function;

@ApplicationScoped
public class EmailToMailMapper implements Function<Email, Mail> {

    @Override
    public Mail apply(Email email) {
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
}
