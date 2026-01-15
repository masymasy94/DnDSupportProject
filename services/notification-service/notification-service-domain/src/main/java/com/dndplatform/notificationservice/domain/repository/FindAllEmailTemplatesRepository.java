package com.dndplatform.notificationservice.domain.repository;

import com.dndplatform.notificationservice.domain.model.EmailTemplateResult;

import java.util.List;

public interface FindAllEmailTemplatesRepository {
    List<EmailTemplateResult> findAll();
}
