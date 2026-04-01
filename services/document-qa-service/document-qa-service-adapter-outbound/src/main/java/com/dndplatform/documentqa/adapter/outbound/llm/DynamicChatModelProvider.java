package com.dndplatform.documentqa.adapter.outbound.llm;

import com.dndplatform.documentqa.domain.model.LlmConfiguration;
import com.dndplatform.documentqa.domain.model.LlmProvider;
import com.dndplatform.documentqa.domain.repository.LlmConfigurationRepository;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@ApplicationScoped
public class DynamicChatModelProvider {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final ConcurrentHashMap<String, ChatModel> cache = new ConcurrentHashMap<>();

    @Inject
    LlmConfigurationRepository llmConfigurationRepository;

    @ConfigProperty(name = "llm.groq-api-key", defaultValue = "")
    String groqApiKey;

    public ChatModel getModel(Long userId) {
        // Try user-specific config first
        if (userId != null) {
            String userKey = "user:" + userId;
            ChatModel cached = cache.get(userKey);
            if (cached != null) {
                return cached;
            }

            Optional<LlmConfiguration> userConfig = llmConfigurationRepository.findActiveUserConfiguration(userId);
            if (userConfig.isPresent()) {
                ChatModel model = createModel(userConfig.get());
                cache.put(userKey, model);
                log.info(() -> "Created chat model for user %d: %s/%s".formatted(
                        userId, userConfig.get().provider(), userConfig.get().modelName()));
                return model;
            }
        }

        // Fallback to system config
        String systemKey = "system";
        ChatModel cached = cache.get(systemKey);
        if (cached != null) {
            return cached;
        }

        LlmConfiguration systemConfig = llmConfigurationRepository.findActiveSystemConfiguration()
                .orElseThrow(() -> new RuntimeException("No active LLM configuration found"));

        ChatModel model = createModel(systemConfig);
        cache.put(systemKey, model);
        log.info(() -> "Created system chat model: %s/%s".formatted(
                systemConfig.provider(), systemConfig.modelName()));
        return model;
    }

    public void invalidateCache() {
        cache.clear();
        log.info("LLM model cache invalidated");
    }

    private ChatModel createModel(LlmConfiguration config) {
        return switch (config.provider()) {
            case GROQ, OPENAI -> {
                String apiKey = config.apiKey() != null && !config.apiKey().isBlank()
                        ? config.apiKey()
                        : groqApiKey;
                yield OpenAiChatModel.builder()
                    .baseUrl(config.baseUrl())
                    .apiKey(apiKey)
                    .modelName(config.modelName())
                    .timeout(Duration.ofSeconds(30))
                    .build();
            }
            case ANTHROPIC -> OpenAiChatModel.builder()
                    .baseUrl("https://api.anthropic.com/v1")
                    .apiKey(config.apiKey())
                    .modelName(config.modelName())
                    .timeout(Duration.ofSeconds(120))
                    .build();
        };
    }
}
