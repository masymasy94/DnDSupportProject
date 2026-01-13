package com.dndplatform.notificationservice.domain.repository;

public interface EmailTemplateExistsByNameRepository {

    void checkNameNotExists(String name);
}