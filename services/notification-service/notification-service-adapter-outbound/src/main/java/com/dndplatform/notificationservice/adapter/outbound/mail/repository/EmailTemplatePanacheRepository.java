package com.dndplatform.notificationservice.adapter.outbound.mail.repository;

import com.dndplatform.notificationservice.adapter.outbound.mail.entity.EmailTemplateEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class EmailTemplatePanacheRepository implements PanacheRepository<EmailTemplateEntity> {

    public Optional<EmailTemplateEntity> findByName(String name) {
        return find("name", name).firstResultOptional();
    }

    public Optional<EmailTemplateEntity> findActiveByName(String name) {
        return find("name = ?1 and active = true", name).firstResultOptional();
    }
}
