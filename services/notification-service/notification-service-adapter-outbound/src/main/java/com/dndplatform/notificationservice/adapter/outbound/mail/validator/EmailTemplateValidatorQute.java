package com.dndplatform.notificationservice.adapter.outbound.mail.validator;

import com.dndplatform.common.exception.InvalidTemplateException;
import com.dndplatform.notificationservice.domain.validator.EmailTemplateValidator;
import io.quarkus.qute.Engine;
import io.quarkus.qute.TemplateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class EmailTemplateValidatorQute implements EmailTemplateValidator {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final Engine engine;

    @Inject
    public EmailTemplateValidatorQute(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void validateSyntax(String htmlContent) {
        log.info("Validating template syntax");
        try {
            engine.parse(htmlContent);
            log.info("Template syntax is valid");
        } catch (TemplateException e) {
            log.warning(() -> "Template syntax validation failed: " + e.getMessage());
            throw new InvalidTemplateException("Invalid template syntax: " + e.getMessage(), e);
        }
    }
}
