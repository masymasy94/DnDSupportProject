package com.dndplatform.notificationservice.domain.repository;

import com.dndplatform.notificationservice.domain.model.EmailTemplate;
import com.dndplatform.notificationservice.domain.model.EmailTemplateResult;

public interface EmailTemplateCreateRepository {

    EmailTemplateResult save(EmailTemplate emailTemplate);
}