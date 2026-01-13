package com.dndplatform.notificationservice.adapter.outbound.mail.repository;

import com.dndplatform.notificationservice.adapter.outbound.mail.entity.EmailTemplateEntity;
import com.dndplatform.notificationservice.domain.repository.EmailTemplateRenderRepository;
import io.quarkus.qute.Engine;
import io.quarkus.qute.Template;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class EmailTemplateRenderRepositoryQute implements EmailTemplateRenderRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final Engine engine;
    private final EmailTemplatePanacheRepository emailTemplateRepository;

    @Inject
    public EmailTemplateRenderRepositoryQute(Engine engine, EmailTemplatePanacheRepository emailTemplateRepository) {
        this.engine = engine;
        this.emailTemplateRepository = emailTemplateRepository;
    }

    @Override
    public String render(String templateName, Map<String, Object> data) {
        log.info(() -> "Rendering template: " + templateName);

        // First, try to find the template in the database
        Optional<EmailTemplateEntity> dbTemplate = emailTemplateRepository.findActiveByName(templateName);
        if (dbTemplate.isPresent()) {
            log.info(() -> "Using database template: " + templateName);
            return renderFromDatabase(dbTemplate.get(), data);
        }

        // Fallback to file-based template
        log.info(() -> "Using file-based template: " + templateName);
        Template template = engine.getTemplate("emails/" + templateName);
        if (template == null) {
            throw new IllegalArgumentException("Email template not found: " + templateName);
        }

        var instance = template.instance();
        if (data != null) {
            data.forEach(instance::data);
        }

        return instance.render();
    }

    private String renderFromDatabase(EmailTemplateEntity templateEntity, Map<String, Object> data) {
        Template template = engine.parse(templateEntity.getHtmlContent());
        var instance = template.instance();
        if (data != null) {
            data.forEach(instance::data);
        }
        return instance.render();
    }
}
