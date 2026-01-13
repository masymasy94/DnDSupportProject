package com.dndplatform.notificationservice.domain.impl;

import com.dndplatform.notificationservice.domain.CreateEmailTemplateService;
import com.dndplatform.notificationservice.domain.model.EmailTemplate;
import com.dndplatform.notificationservice.domain.model.EmailTemplateResult;
import com.dndplatform.notificationservice.domain.repository.EmailTemplateRepository;
import com.dndplatform.notificationservice.domain.validator.EmailTemplateValidator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class CreateEmailTemplateServiceImpl implements CreateEmailTemplateService {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EmailTemplateValidator emailTemplateValidator;
    private final EmailTemplateRepository emailTemplateRepository;

    @Inject
    public CreateEmailTemplateServiceImpl(EmailTemplateValidator emailTemplateValidator,
                                          EmailTemplateRepository emailTemplateRepository) {
        this.emailTemplateValidator = emailTemplateValidator;
        this.emailTemplateRepository = emailTemplateRepository;
    }

    @Override
    public EmailTemplateResult create(EmailTemplate emailTemplate) {
        log.info(() -> "Creating email template: " + emailTemplate.name());

        emailTemplateValidator.validateSyntax(emailTemplate.htmlContent());
        emailTemplateRepository.checkNameNotExists(emailTemplate.name());

        return emailTemplateRepository.save(emailTemplate);
    }
}
