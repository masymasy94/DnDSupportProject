package com.dndplatform.documentqa.adapter.inbound.rest.llm;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.documentqa.domain.LlmConfigurationService;
import com.dndplatform.documentqa.domain.model.LlmConfiguration;
import com.dndplatform.documentqa.domain.model.LlmConfigurationBuilder;
import com.dndplatform.documentqa.domain.model.LlmProvider;
import com.dndplatform.documentqa.view.model.LlmConfigurationResource;
import com.dndplatform.documentqa.view.model.vm.CreateLlmConfigurationRequest;
import com.dndplatform.documentqa.view.model.vm.LlmConfigurationViewModel;
import com.dndplatform.documentqa.view.model.vm.LlmConfigurationViewModelBuilder;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

@Delegate
@RequestScoped
public class LlmConfigurationDelegate implements LlmConfigurationResource {

    private final LlmConfigurationService service;

    @Inject
    public LlmConfigurationDelegate(LlmConfigurationService service) {
        this.service = service;
    }

    @Override
    public List<LlmConfigurationViewModel> listSystemConfigurations() {
        return service.getSystemConfigurations().stream().map(this::toViewModel).toList();
    }

    @Override
    public LlmConfigurationViewModel createSystemConfiguration(CreateLlmConfigurationRequest request) {
        LlmConfiguration config = toDomain(request, null);
        return toViewModel(service.createSystemConfiguration(config));
    }

    @Override
    public void activateSystemConfiguration(Long id) {
        service.activateSystemConfiguration(id);
    }

    @Override
    public void deleteSystemConfiguration(Long id) {
        service.deleteSystemConfiguration(id);
    }

    @Override
    public List<LlmConfigurationViewModel> listUserConfigurations(Long userId) {
        return service.getUserConfigurations(userId).stream().map(this::toViewModel).toList();
    }

    @Override
    public LlmConfigurationViewModel createUserConfiguration(CreateLlmConfigurationRequest request) {
        LlmConfiguration config = toDomain(request, request.userId());
        return toViewModel(service.createUserConfiguration(config));
    }

    @Override
    public void activateUserConfiguration(Long id, Long userId) {
        service.activateUserConfiguration(id, userId);
    }

    @Override
    public void deleteUserConfiguration(Long id, Long userId) {
        service.deleteUserConfiguration(id, userId);
    }

    private LlmConfigurationViewModel toViewModel(LlmConfiguration config) {
        return LlmConfigurationViewModelBuilder.builder()
                .withId(config.id())
                .withUserId(config.userId())
                .withName(config.name())
                .withProvider(config.provider().name())
                .withModelName(config.modelName())
                .withBaseUrl(config.baseUrl())
                .withEmbeddingProvider(config.embeddingProvider())
                .withEmbeddingModel(config.embeddingModel())
                .withActive(config.active())
                .withCreatedAt(config.createdAt())
                .withUpdatedAt(config.updatedAt())
                .build();
    }

    private LlmConfiguration toDomain(CreateLlmConfigurationRequest request, Long userId) {
        return LlmConfigurationBuilder.builder()
                .withUserId(userId)
                .withName(request.name())
                .withProvider(LlmProvider.valueOf(request.provider()))
                .withModelName(request.modelName())
                .withBaseUrl(request.baseUrl())
                .withApiKey(request.apiKey())
                .withEmbeddingProvider(request.embeddingProvider())
                .withEmbeddingModel(request.embeddingModel())
                .withActive(false)
                .build();
    }
}
