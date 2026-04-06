package com.dndplatform.documentqa.adapter.inbound.rest.llm;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.documentqa.domain.LlmConfigurationService;
import com.dndplatform.documentqa.domain.model.LlmConfiguration;
import com.dndplatform.documentqa.domain.model.LlmProvider;
import com.dndplatform.documentqa.view.model.vm.CreateLlmConfigurationRequest;
import com.dndplatform.documentqa.view.model.vm.LlmConfigurationViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class LlmConfigurationDelegateTest {

    @Mock
    private LlmConfigurationService service;

    private LlmConfigurationDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new LlmConfigurationDelegate(service);
    }

    @Test
    void shouldReturnSystemConfigurations(@Random LlmConfiguration cfg1, @Random LlmConfiguration cfg2) {
        given(service.getSystemConfigurations()).willReturn(List.of(cfg1, cfg2));

        List<LlmConfigurationViewModel> result = sut.listSystemConfigurations();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(cfg1.id());
        assertThat(result.get(1).id()).isEqualTo(cfg2.id());

        then(service).should().getSystemConfigurations();
    }

    @Test
    void shouldCreateSystemConfiguration() {
        CreateLlmConfigurationRequest request = new CreateLlmConfigurationRequest(
                null, "My Config", "GROQ", "llama3-8b-8192", null, "sk-test", null, null
        );
        LlmConfiguration created = new LlmConfiguration(
                1L, null, "My Config", LlmProvider.GROQ, "llama3-8b-8192",
                null, "sk-test", null, null, false, LocalDateTime.now(), LocalDateTime.now()
        );
        given(service.createSystemConfiguration(any(LlmConfiguration.class))).willReturn(created);

        LlmConfigurationViewModel result = sut.createSystemConfiguration(request);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("My Config");
        assertThat(result.provider()).isEqualTo("GROQ");

        then(service).should().createSystemConfiguration(any(LlmConfiguration.class));
    }

    @Test
    void shouldActivateSystemConfiguration(@Random Long id) {
        willDoNothing().given(service).activateSystemConfiguration(id);

        sut.activateSystemConfiguration(id);

        then(service).should().activateSystemConfiguration(id);
    }

    @Test
    void shouldDeleteSystemConfiguration(@Random Long id) {
        willDoNothing().given(service).deleteSystemConfiguration(id);

        sut.deleteSystemConfiguration(id);

        then(service).should().deleteSystemConfiguration(id);
    }
}
