package com.dndplatform.notificationservice.adapter.outbound.mail.repository;

import com.dndplatform.notificationservice.adapter.outbound.mail.entity.EmailTemplateEntity;
import com.dndplatform.notificationservice.domain.model.EmailTemplateResult;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.function.Function;

@ApplicationScoped
public class EmailTemplateEntityToResultMapper implements Function<EmailTemplateEntity, EmailTemplateResult> {

    @Override
    public EmailTemplateResult apply(EmailTemplateEntity entity) {
        return new EmailTemplateResult(
                entity.id,
                entity.getName(),
                entity.getCreatedAt()
        );
    }
}
