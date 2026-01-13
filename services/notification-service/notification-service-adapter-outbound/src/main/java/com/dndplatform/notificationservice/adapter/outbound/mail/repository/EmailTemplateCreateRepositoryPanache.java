package com.dndplatform.notificationservice.adapter.outbound.mail.repository;

import com.dndplatform.notificationservice.domain.model.EmailTemplate;
import com.dndplatform.notificationservice.domain.model.EmailTemplateResult;
import com.dndplatform.notificationservice.domain.repository.EmailTemplateCreateRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class EmailTemplateCreateRepositoryPanache implements EmailTemplateCreateRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EmailTemplatePanacheRepository panacheRepository;
    private final EmailTemplateToEntityMapper toEntityMapper;
    private final EmailTemplateEntityToResultMapper toResultMapper;

    @Inject
    public EmailTemplateCreateRepositoryPanache(EmailTemplatePanacheRepository panacheRepository,
                                                EmailTemplateToEntityMapper toEntityMapper,
                                                EmailTemplateEntityToResultMapper toResultMapper) {
        this.panacheRepository = panacheRepository;
        this.toEntityMapper = toEntityMapper;
        this.toResultMapper = toResultMapper;
    }

    @Override
    @Transactional
    public EmailTemplateResult save(EmailTemplate emailTemplate) {
        log.info(() -> "Saving email template: " + emailTemplate.name());

        var entity = toEntityMapper.apply(emailTemplate);
        panacheRepository.persist(entity);

        return toResultMapper.apply(entity);
    }
}
