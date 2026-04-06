package com.dndplatform.documentqa.adapter.outbound.jpa.repository;

import com.dndplatform.documentqa.adapter.outbound.jpa.entity.LlmConfigurationEntity;
import com.dndplatform.documentqa.domain.model.LlmConfiguration;
import com.dndplatform.documentqa.domain.model.LlmConfigurationBuilder;
import com.dndplatform.documentqa.domain.model.LlmProvider;
import com.dndplatform.documentqa.domain.repository.LlmConfigurationRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class LlmConfigurationRepositoryJpa implements LlmConfigurationRepository {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final LlmConfigurationPanacheRepository panacheRepository;

    @Inject
    public LlmConfigurationRepositoryJpa(LlmConfigurationPanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    public List<LlmConfiguration> findSystemConfigurations() {
        log.info("Finding system LLM configurations");

        List<LlmConfigurationEntity> entities = panacheRepository.findSystemConfigurations();
        return entities.stream().map(this::toDomain).toList();
    }

    @Override
    public List<LlmConfiguration> findUserConfigurations(Long userId) {
        log.info(() -> "Finding LLM configurations for user: %d".formatted(userId));

        List<LlmConfigurationEntity> entities = panacheRepository.findUserConfigurations(userId);
        return entities.stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<LlmConfiguration> findById(Long id) {
        log.info(() -> "Finding LLM configuration by ID: %d".formatted(id));

        return panacheRepository.findByIdOptional(id).map(this::toDomain);
    }

    @Override
    public Optional<LlmConfiguration> findActiveSystemConfiguration() {
        log.info("Finding active system LLM configuration");

        return panacheRepository.findActiveSystemConfiguration().map(this::toDomain);
    }

    @Override
    public Optional<LlmConfiguration> findActiveUserConfiguration(Long userId) {
        log.info(() -> "Finding active LLM configuration for user: %d".formatted(userId));

        return panacheRepository.findActiveUserConfiguration(userId).map(this::toDomain);
    }

    @Override
    @Transactional
    public LlmConfiguration create(LlmConfiguration config) {
        log.info(() -> "Creating LLM configuration: %s".formatted(config.name()));

        LlmConfigurationEntity entity = new LlmConfigurationEntity();
        entity.userId = config.userId();
        entity.name = config.name();
        entity.provider = config.provider().name();
        entity.modelName = config.modelName();
        entity.baseUrl = config.baseUrl();
        entity.apiKeyEncrypted = config.apiKey();
        entity.embeddingProvider = config.embeddingProvider();
        entity.embeddingModel = config.embeddingModel();
        entity.active = config.active();

        panacheRepository.persist(entity);

        log.info(() -> "LLM configuration created with ID: %d".formatted(entity.id));
        return toDomain(entity);
    }

    @Override
    @Transactional
    public void activate(Long id) {
        log.info(() -> "Activating LLM configuration: %d".formatted(id));

        LlmConfigurationEntity entity = panacheRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("LLM configuration not found with ID: %d".formatted(id)));

        entity.active = true;
        entity.updatedAt = LocalDateTime.now();

        log.info(() -> "LLM configuration %d activated".formatted(id));
    }

    @Override
    @Transactional
    public void deactivateAllSystem() {
        log.info("Deactivating all system LLM configurations");

        panacheRepository.update("active = false, updatedAt = ?1 WHERE userId IS NULL",
                LocalDateTime.now());
    }

    @Override
    @Transactional
    public void deactivateAllForUser(Long userId) {
        log.info(() -> "Deactivating all LLM configurations for user: %d".formatted(userId));

        panacheRepository.update("active = false, updatedAt = ?1 WHERE userId = ?2",
                LocalDateTime.now(), userId);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info(() -> "Deleting LLM configuration: %d".formatted(id));

        LlmConfigurationEntity entity = panacheRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("LLM configuration not found with ID: %d".formatted(id)));

        panacheRepository.delete(entity);
        log.info(() -> "LLM configuration %d deleted successfully".formatted(id));
    }

    private LlmConfiguration toDomain(LlmConfigurationEntity entity) {
        return LlmConfigurationBuilder.builder()
                .withId(entity.id)
                .withUserId(entity.userId)
                .withName(entity.name)
                .withProvider(LlmProvider.valueOf(entity.provider))
                .withModelName(entity.modelName)
                .withBaseUrl(entity.baseUrl)
                .withApiKey(entity.apiKeyEncrypted)
                .withEmbeddingProvider(entity.embeddingProvider)
                .withEmbeddingModel(entity.embeddingModel)
                .withActive(entity.active)
                .withCreatedAt(entity.createdAt)
                .withUpdatedAt(entity.updatedAt)
                .build();
    }
}
