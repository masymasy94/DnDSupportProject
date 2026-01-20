package com.dndplatform.notificationservice.domain;

import com.dndplatform.notificationservice.domain.model.EmailTemplate;
import com.dndplatform.notificationservice.domain.model.EmailTemplateResult;

public interface CreateEmailTemplateService {
    EmailTemplateResult create(EmailTemplate emailTemplate);
}
