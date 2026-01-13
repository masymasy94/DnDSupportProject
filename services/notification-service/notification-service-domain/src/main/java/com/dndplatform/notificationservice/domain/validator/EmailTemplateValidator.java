package com.dndplatform.notificationservice.domain.validator;

public interface EmailTemplateValidator {
    /**
     * Validates the Qute template syntax.
     * @param htmlContent the template HTML content to validate
     * @throws com.dndplatform.common.exception.InvalidTemplateException if syntax is invalid
     */
    void validateSyntax(String htmlContent);
}
