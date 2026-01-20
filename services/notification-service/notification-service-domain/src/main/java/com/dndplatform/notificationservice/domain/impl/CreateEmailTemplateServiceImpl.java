package com.dndplatform.notificationservice.domain.impl;

import com.dndplatform.notificationservice.domain.CreateEmailTemplateService;
import com.dndplatform.notificationservice.domain.model.EmailTemplate;
import com.dndplatform.notificationservice.domain.model.EmailTemplateResult;
import com.dndplatform.notificationservice.domain.repository.EmailTemplateExistsByNameRepository;
import com.dndplatform.notificationservice.domain.repository.EmailTemplateCreateRepository;
import com.dndplatform.notificationservice.domain.validator.EmailTemplateValidator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class CreateEmailTemplateServiceImpl implements CreateEmailTemplateService {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EmailTemplateValidator emailTemplateValidator;
    private final EmailTemplateExistsByNameRepository emailTemplateExistsByNameRepository;
    private final EmailTemplateCreateRepository emailTemplateCreateRepository;

    @Inject
    public CreateEmailTemplateServiceImpl(EmailTemplateValidator emailTemplateValidator,
                                          EmailTemplateExistsByNameRepository emailTemplateExistsByNameRepository,
                                          EmailTemplateCreateRepository emailTemplateCreateRepository) {
        this.emailTemplateValidator = emailTemplateValidator;
        this.emailTemplateExistsByNameRepository = emailTemplateExistsByNameRepository;
        this.emailTemplateCreateRepository = emailTemplateCreateRepository;
    }

    @Override
    public EmailTemplateResult create(EmailTemplate emailTemplate) {

        emailTemplateValidator.validateSyntax(emailTemplate.htmlContent());
        emailTemplateExistsByNameRepository.checkNameNotExists(emailTemplate.name());

        return emailTemplateCreateRepository.save(emailTemplate);
    }
}
