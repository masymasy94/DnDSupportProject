package com.dndplatform.documentqa.adapter.outbound.text;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

@ApplicationScoped
public class PlainTextExtractor {

    private final Logger log = Logger.getLogger(getClass().getName());

    public String extract(InputStream inputStream) {
        try {
            String text = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            log.info(() -> "Extracted %d characters from plain text".formatted(text.length()));
            return text;
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract plain text", e);
        }
    }
}
