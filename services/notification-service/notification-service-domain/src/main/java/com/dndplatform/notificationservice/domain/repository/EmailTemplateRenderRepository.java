package com.dndplatform.notificationservice.domain.repository;

import java.util.Map;

public interface EmailTemplateRenderRepository {
    String render(String templateName, Map<String, Object> data);
}
