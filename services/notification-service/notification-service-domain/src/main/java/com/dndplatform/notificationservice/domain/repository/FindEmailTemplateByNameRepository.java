package com.dndplatform.notificationservice.domain.repository;

import com.dndplatform.notificationservice.domain.model.EmailTemplateDetails;

import java.util.Optional;

public interface FindEmailTemplateByNameRepository {

    Optional<EmailTemplateDetails> findByName(String name);
}