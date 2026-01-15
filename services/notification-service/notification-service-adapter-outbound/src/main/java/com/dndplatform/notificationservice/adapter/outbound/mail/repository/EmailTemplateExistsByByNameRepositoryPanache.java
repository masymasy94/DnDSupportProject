package com.dndplatform.notificationservice.adapter.outbound.mail.repository;

import com.dndplatform.common.exception.ConflictException;
import com.dndplatform.notificationservice.domain.repository.EmailTemplateExistsByNameRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class EmailTemplateExistsByByNameRepositoryPanache implements EmailTemplateExistsByNameRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EmailTemplatePanacheRepository panacheRepository;

    @Inject
    public EmailTemplateExistsByByNameRepositoryPanache(EmailTemplatePanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    public void checkNameNotExists(String name) {
        log.info(() -> "Checking if template name exists: " + name);

        if (panacheRepository.findByName(name).isPresent()) {
            throw new ConflictException("Email template with name '" + name + "' already exists");
        }
    }
}
