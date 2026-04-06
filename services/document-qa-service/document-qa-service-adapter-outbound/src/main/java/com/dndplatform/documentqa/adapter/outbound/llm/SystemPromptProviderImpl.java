package com.dndplatform.documentqa.adapter.outbound.llm;

import com.dndplatform.documentqa.domain.impl.SystemPromptProvider;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

@ApplicationScoped
public class SystemPromptProviderImpl implements SystemPromptProvider {

    private final Logger log = Logger.getLogger(getClass().getName());

    private volatile String cachedPrompt;

    @Override
    public String getSystemPrompt() {
        if (cachedPrompt != null) {
            return cachedPrompt;
        }

        synchronized (this) {
            if (cachedPrompt != null) {
                return cachedPrompt;
            }

            try (InputStream is = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("prompts/dnd-system-prompt.txt")) {
                if (is == null) {
                    throw new RuntimeException("System prompt resource not found: prompts/dnd-system-prompt.txt");
                }
                cachedPrompt = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                log.info(() -> "Loaded system prompt (%d characters)".formatted(cachedPrompt.length()));
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException("Failed to load system prompt", e);
            }
        }

        return cachedPrompt;
    }
}
