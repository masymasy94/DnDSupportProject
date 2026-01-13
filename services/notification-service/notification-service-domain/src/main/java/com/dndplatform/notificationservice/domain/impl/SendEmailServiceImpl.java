package com.dndplatform.notificationservice.domain.impl;

import com.dndplatform.notificationservice.domain.SendEmailService;
import com.dndplatform.notificationservice.domain.model.Email;
import com.dndplatform.notificationservice.domain.model.EmailBuilder;
import com.dndplatform.notificationservice.domain.model.EmailResult;
import com.dndplatform.notificationservice.domain.repository.EmailSendRepository;
import com.dndplatform.notificationservice.domain.repository.EmailTemplateRenderRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class SendEmailServiceImpl implements SendEmailService {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EmailSendRepository emailSendRepository;
    private final EmailTemplateRenderRepository emailTemplateRenderRepository;

    @Inject
    public SendEmailServiceImpl(EmailSendRepository emailSendRepository,
                                EmailTemplateRenderRepository emailTemplateRenderRepository) {
        this.emailSendRepository = emailSendRepository;
        this.emailTemplateRenderRepository = emailTemplateRenderRepository;
    }

    @Override
    public EmailResult send(Email email) {
        log.info(() -> "Sending email to: " + email.to());

        // log mail to db
        // choose template on db
        // send
        Email emailToSend = processTemplate(email);
        return emailSendRepository.send(emailToSend);
    }

    private Email processTemplate(Email email) {
        if (email.templateName() != null && !email.templateName().isBlank()) {
            log.info(() -> "Rendering template: " + email.templateName());
            String htmlContent = emailTemplateRenderRepository.render(
                    email.templateName(),
                    email.templateData()
            );
            return EmailBuilder
                    .toBuilder(email)
                    .withHtmlBody(htmlContent)
                    .build();
        }
        return email;
    }
}
