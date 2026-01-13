package com.dndplatform.notificationservice.domain.repository;

import com.dndplatform.notificationservice.domain.model.EmailTemplateDetails;

public interface FindEmailTemplateByIdRepository {

    /**
     * Finds an email template by its ID.
     * @param id the template ID
     * @return the template details
     * @throws com.dndplatform.common.exception.NotFoundException if template not found
     */
    EmailTemplateDetails findById(Long id);
}
