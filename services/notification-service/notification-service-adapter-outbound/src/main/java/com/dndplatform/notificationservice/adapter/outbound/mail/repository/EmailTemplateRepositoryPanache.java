package com.dndplatform.notificationservice.adapter.outbound.mail.repository;

import com.dndplatform.common.exception.ConflictException;
import com.dndplatform.notificationservice.adapter.outbound.mail.entity.EmailTemplateEntity;
import com.dndplatform.notificationservice.domain.model.EmailTemplate;
import com.dndplatform.notificationservice.domain.model.EmailTemplateResult;
import com.dndplatform.notificationservice.domain.repository.EmailTemplateRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
public class EmailTemplateRepositoryPanache implements EmailTemplateRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EmailTemplatePanacheRepository panacheRepository;

    @Inject
    public EmailTemplateRepositoryPanache(EmailTemplatePanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    @Transactional
    public EmailTemplateResult save(EmailTemplate emailTemplate) {
        log.info(() -> "Saving email template: " + emailTemplate.name());

        EmailTemplateEntity entity = new EmailTemplateEntity();
        entity.setName(emailTemplate.name());
        entity.setSubject(emailTemplate.subject());
        entity.setHtmlContent(emailTemplate.htmlContent());
        entity.setDescription(emailTemplate.description());
        entity.setActive(true);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        panacheRepository.persist(entity);

        log.info(() -> "Email template saved with id: " + entity.id);

        return new EmailTemplateResult(
                entity.id,
                entity.getName(),
                entity.getCreatedAt()
        );
    }

    @Override
    public void checkNameNotExists(String name) {
        log.info(() -> "Checking if template name exists: " + name);

        if (panacheRepository.findByName(name).isPresent()) {
            throw new ConflictException("Email template with name '" + name + "' already exists");
        }
    }
}
