package com.dndplatform.notificationservice.adapter.outbound.mail.repository;

import com.dndplatform.notificationservice.adapter.outbound.mail.entity.EmailTemplateEntity;
import com.dndplatform.notificationservice.domain.model.EmailTemplate;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.function.Function;

@ApplicationScoped
public class EmailTemplateToEntityMapper implements Function<EmailTemplate, EmailTemplateEntity> {

    @Override
    public EmailTemplateEntity apply(EmailTemplate emailTemplate) {
        EmailTemplateEntity entity = new EmailTemplateEntity();
        entity.setName(emailTemplate.name());
        entity.setSubject(emailTemplate.subject());
        entity.setHtmlContent(emailTemplate.htmlContent());
        entity.setDescription(emailTemplate.description());
        entity.setActive(true);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
