package com.dndplatform.notificationservice.domain.impl;

import com.dndplatform.notificationservice.domain.SendEmailService;
import com.dndplatform.notificationservice.domain.model.Email;
import com.dndplatform.notificationservice.domain.model.EmailBuilder;
import com.dndplatform.notificationservice.domain.model.EmailResult;
import com.dndplatform.notificationservice.domain.model.EmailTemplateDetails;
import com.dndplatform.notificationservice.domain.repository.EmailSendRepository;
import com.dndplatform.notificationservice.domain.repository.FindEmailTemplateByIdRepository;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class SendEmailServiceImpl implements SendEmailService {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EmailSendRepository emailSendRepository;
    private final FindEmailTemplateByIdRepository findEmailTemplateByIdRepository;

    @Inject
    public SendEmailServiceImpl(EmailSendRepository emailSendRepository,
                                FindEmailTemplateByIdRepository findEmailTemplateByIdRepository) {
        this.emailSendRepository = emailSendRepository;
        this.findEmailTemplateByIdRepository = findEmailTemplateByIdRepository;
    }

    @Override
    public EmailResult send(Email email) {
        log.info(() -> "Sending email to: " + email.to());

        var template = findEmailTemplateByIdRepository.findById(email.templateId());
        return emailSendRepository.send(getEmail(email, template));
    }


    @Nonnull
    private static Email getEmail(Email email, EmailTemplateDetails template) {
        return EmailBuilder
                .toBuilder(email)
                .withSubject(template.subject())
                .withHtmlBody(template.htmlContent())
                .build();
    }
}
