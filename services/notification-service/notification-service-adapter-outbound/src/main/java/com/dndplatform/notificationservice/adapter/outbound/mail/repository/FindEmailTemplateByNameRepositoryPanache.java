package com.dndplatform.notificationservice.adapter.outbound.mail.repository;

import com.dndplatform.notificationservice.domain.model.EmailTemplateDetails;
import com.dndplatform.notificationservice.domain.repository.FindEmailTemplateByNameRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class FindEmailTemplateByNameRepositoryPanache implements FindEmailTemplateByNameRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EmailTemplatePanacheRepository panacheRepository;

    @Inject
    public FindEmailTemplateByNameRepositoryPanache(EmailTemplatePanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    public Optional<EmailTemplateDetails> findByName(String name) {
        log.info(() -> "Finding email template by name: " + name);

        return panacheRepository.findActiveByName(name)
                .map(entity -> new EmailTemplateDetails(
                        entity.id,
                        entity.getName(),
                        entity.getSubject(),
                        entity.getHtmlContent()
                ));
    }
}