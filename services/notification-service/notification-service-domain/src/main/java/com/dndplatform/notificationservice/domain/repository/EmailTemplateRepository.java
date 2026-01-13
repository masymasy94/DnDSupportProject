package com.dndplatform.notificationservice.domain.repository;

import com.dndplatform.notificationservice.domain.model.EmailTemplate;
import com.dndplatform.notificationservice.domain.model.EmailTemplateResult;

public interface EmailTemplateRepository {

    /**
     * Saves a new email template.
     * @param emailTemplate the template to save
     * @return the result with generated id and creation timestamp
     */
    EmailTemplateResult save(EmailTemplate emailTemplate);

    /**
     * Checks if a template with the given name already exists.
     * @param name the template name to check
     * @throws com.dndplatform.common.exception.ConflictException if name already exists
     */
    void checkNameNotExists(String name);
}
