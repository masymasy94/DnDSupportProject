package com.dndplatform.notificationservice.adapter.outbound.mail.repository;

import com.dndplatform.notificationservice.domain.model.EmailTemplateResult;
import com.dndplatform.notificationservice.domain.repository.FindAllEmailTemplatesRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class FindAllEmailTemplatesRepositoryPanache implements FindAllEmailTemplatesRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EmailTemplatePanacheRepository panacheRepository;
    private final EmailTemplateEntityToResultMapper mapper;

    @Inject
    public FindAllEmailTemplatesRepositoryPanache(EmailTemplatePanacheRepository panacheRepository,
                                                  EmailTemplateEntityToResultMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public List<EmailTemplateResult> findAll() {
        log.info(() -> "Retrieving all email templates");

        return panacheRepository.listAll().stream()
                .map(mapper)
                .toList();
    }
}
