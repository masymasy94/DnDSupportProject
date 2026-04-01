package com.dndplatform.documentqa.domain;

import com.dndplatform.documentqa.domain.model.LlmConfiguration;

import java.util.List;

public interface LlmConfigurationService {

    List<LlmConfiguration> getSystemConfigurations();

    List<LlmConfiguration> getUserConfigurations(Long userId);

    LlmConfiguration createSystemConfiguration(LlmConfiguration config);

    LlmConfiguration createUserConfiguration(LlmConfiguration config);

    void activateSystemConfiguration(Long id);

    void activateUserConfiguration(Long id, Long userId);

    void deleteSystemConfiguration(Long id);

    void deleteUserConfiguration(Long id, Long userId);
}
