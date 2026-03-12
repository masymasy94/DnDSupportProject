package com.dndplatform.documentqa.domain.impl;

import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.documentqa.domain.LlmConfigurationService;
import com.dndplatform.documentqa.domain.model.LlmConfiguration;
import com.dndplatform.documentqa.domain.repository.LlmConfigurationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class LlmConfigurationServiceImpl implements LlmConfigurationService {

    private final LlmConfigurationRepository repository;

    @Inject
    public LlmConfigurationServiceImpl(LlmConfigurationRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<LlmConfiguration> getSystemConfigurations() {
        return repository.findSystemConfigurations();
    }

    @Override
    public List<LlmConfiguration> getUserConfigurations(Long userId) {
        return repository.findUserConfigurations(userId);
    }

    @Override
    public LlmConfiguration createSystemConfiguration(LlmConfiguration config) {
        return repository.create(config);
    }

    @Override
    public LlmConfiguration createUserConfiguration(LlmConfiguration config) {
        return repository.create(config);
    }

    @Override
    public void activateSystemConfiguration(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Configuration not found: " + id));
        repository.deactivateAllSystem();
        repository.activate(id);
    }

    @Override
    public void activateUserConfiguration(Long id, Long userId) {
        LlmConfiguration config = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Configuration not found: " + id));
        if (!userId.equals(config.userId())) {
            throw new ForbiddenException("Access denied to configuration: " + id);
        }
        repository.deactivateAllForUser(userId);
        repository.activate(id);
    }

    @Override
    public void deleteSystemConfiguration(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Configuration not found: " + id));
        repository.deleteById(id);
    }

    @Override
    public void deleteUserConfiguration(Long id, Long userId) {
        LlmConfiguration config = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Configuration not found: " + id));
        if (!userId.equals(config.userId())) {
            throw new ForbiddenException("Access denied to configuration: " + id);
        }
        repository.deleteById(id);
    }
}
