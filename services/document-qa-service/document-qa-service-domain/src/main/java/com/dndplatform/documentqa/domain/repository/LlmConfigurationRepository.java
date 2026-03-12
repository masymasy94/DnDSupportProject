package com.dndplatform.documentqa.domain.repository;

import com.dndplatform.documentqa.domain.model.LlmConfiguration;

import java.util.List;
import java.util.Optional;

public interface LlmConfigurationRepository {

    List<LlmConfiguration> findSystemConfigurations();

    List<LlmConfiguration> findUserConfigurations(Long userId);

    Optional<LlmConfiguration> findById(Long id);

    Optional<LlmConfiguration> findActiveSystemConfiguration();

    Optional<LlmConfiguration> findActiveUserConfiguration(Long userId);

    LlmConfiguration create(LlmConfiguration config);

    void activate(Long id);

    void deactivateAllSystem();

    void deactivateAllForUser(Long userId);

    void deleteById(Long id);
}
