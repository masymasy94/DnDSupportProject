package com.dndplatform.documentqa.adapter.outbound.llm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SystemPromptProviderImpl")
class SystemPromptProviderImplTest {

    private SystemPromptProviderImpl sut;

    @BeforeEach
    void setUp() {
        sut = new SystemPromptProviderImpl();
    }

    @Test
    @DisplayName("should load system prompt from classpath resource")
    void shouldLoadSystemPromptFromClasspathResource() {
        String prompt = sut.getSystemPrompt();

        assertThat(prompt).isNotNull();
        assertThat(prompt).isNotBlank();
    }

    @Test
    @DisplayName("should return same instance on repeated calls (cached)")
    void shouldReturnSameInstanceOnRepeatedCalls() {
        String first = sut.getSystemPrompt();
        String second = sut.getSystemPrompt();

        assertThat(first).isSameAs(second);
    }
}
