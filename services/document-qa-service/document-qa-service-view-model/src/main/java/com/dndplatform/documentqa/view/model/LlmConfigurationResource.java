package com.dndplatform.documentqa.view.model;

import com.dndplatform.documentqa.view.model.vm.CreateLlmConfigurationRequest;
import com.dndplatform.documentqa.view.model.vm.LlmConfigurationViewModel;
import jakarta.validation.Valid;

import java.util.List;

public interface LlmConfigurationResource {

    List<LlmConfigurationViewModel> listSystemConfigurations();

    LlmConfigurationViewModel createSystemConfiguration(@Valid CreateLlmConfigurationRequest request);

    void activateSystemConfiguration(Long id);

    void deleteSystemConfiguration(Long id);

    List<LlmConfigurationViewModel> listUserConfigurations(Long userId);

    LlmConfigurationViewModel createUserConfiguration(@Valid CreateLlmConfigurationRequest request);

    void activateUserConfiguration(Long id, Long userId);

    void deleteUserConfiguration(Long id, Long userId);
}
