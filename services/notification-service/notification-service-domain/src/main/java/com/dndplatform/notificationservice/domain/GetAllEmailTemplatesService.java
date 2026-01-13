package com.dndplatform.notificationservice.domain;

import com.dndplatform.notificationservice.domain.model.EmailTemplateResult;

import java.util.List;

public interface GetAllEmailTemplatesService {
    List<EmailTemplateResult> getAll();
}
