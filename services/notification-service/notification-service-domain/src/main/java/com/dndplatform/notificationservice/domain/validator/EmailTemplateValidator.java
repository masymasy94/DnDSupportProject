package com.dndplatform.notificationservice.domain.validator;

public interface EmailTemplateValidator {
    void validateSyntax(String htmlContent);
}
