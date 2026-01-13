package com.dndplatform.notificationservice.adapter.outbound.mail.repository;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.notificationservice.domain.model.EmailTemplateDetails;
import com.dndplatform.notificationservice.domain.repository.FindEmailTemplateByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class FindEmailTemplateByIdRepositoryPanache implements FindEmailTemplateByIdRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EmailTemplatePanacheRepository panacheRepository;

    @Inject
    public FindEmailTemplateByIdRepositoryPanache(EmailTemplatePanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    public EmailTemplateDetails findById(Long id) {
        log.info(() -> "Finding email template by id: " + id);

        return panacheRepository.findByIdOptional(id)
                .map(entity -> new EmailTemplateDetails(
                        entity.id,
                        entity.getName(),
                        entity.getSubject(),
                        entity.getHtmlContent()
                ))
                .orElseThrow(() -> new NotFoundException("Email template with id '" + id + "' not found"));
    }
}
