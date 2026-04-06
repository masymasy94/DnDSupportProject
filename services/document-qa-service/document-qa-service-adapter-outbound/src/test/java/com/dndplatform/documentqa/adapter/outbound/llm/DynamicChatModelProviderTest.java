package com.dndplatform.documentqa.adapter.outbound.llm;

import com.dndplatform.documentqa.domain.model.LlmConfiguration;
import com.dndplatform.documentqa.domain.repository.LlmConfigurationRepository;
import dev.langchain4j.model.chat.ChatModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DisplayName("DynamicChatModelProvider")
class DynamicChatModelProviderTest {

    @Mock
    private LlmConfigurationRepository llmConfigurationRepository;

    private DynamicChatModelProvider sut;

    @BeforeEach
    void setUp() throws Exception {
        sut = new DynamicChatModelProvider();

        Field repoField = DynamicChatModelProvider.class.getDeclaredField("llmConfigurationRepository");
        repoField.setAccessible(true);
        repoField.set(sut, llmConfigurationRepository);

        Field keyField = DynamicChatModelProvider.class.getDeclaredField("groqApiKey");
        keyField.setAccessible(true);
        keyField.set(sut, "");
    }

    @SuppressWarnings("unchecked")
    private ConcurrentHashMap<String, ChatModel> getCache() throws Exception {
        Field cacheField = DynamicChatModelProvider.class.getDeclaredField("cache");
        cacheField.setAccessible(true);
        return (ConcurrentHashMap<String, ChatModel>) cacheField.get(sut);
    }

    @Test
    @DisplayName("should throw when no active system configuration found")
    void shouldThrowWhenNoActiveSystemConfigurationFound() {
        given(llmConfigurationRepository.findActiveSystemConfiguration()).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.getModel(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No active LLM configuration found");
    }

    @Test
    @DisplayName("should throw when user has no config and no system config exists")
    void shouldThrowWhenUserHasNoConfigAndNoSystemConfig() {
        Long userId = 1L;
        given(llmConfigurationRepository.findActiveUserConfiguration(userId)).willReturn(Optional.empty());
        given(llmConfigurationRepository.findActiveSystemConfiguration()).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.getModel(userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No active LLM configuration found");
    }

    @Test
    @DisplayName("should return cached model for user without calling repository")
    void shouldReturnCachedModelForUser() throws Exception {
        Long userId = 5L;
        ChatModel mockModel = mock(ChatModel.class);
        getCache().put("user:" + userId, mockModel);

        ChatModel result = sut.getModel(userId);

        assertThat(result).isSameAs(mockModel);
        then(llmConfigurationRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("should return cached system model without calling repository")
    void shouldReturnCachedSystemModel() throws Exception {
        ChatModel mockModel = mock(ChatModel.class);
        getCache().put("system", mockModel);

        ChatModel result = sut.getModel(null);

        assertThat(result).isSameAs(mockModel);
        then(llmConfigurationRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("should return cached system model even when userId is provided but user cache is empty")
    void shouldReturnSystemCachedModelWhenUserHasNoCachedModel() throws Exception {
        Long userId = 10L;
        ChatModel mockModel = mock(ChatModel.class);
        getCache().put("system", mockModel);

        given(llmConfigurationRepository.findActiveUserConfiguration(userId)).willReturn(Optional.empty());

        ChatModel result = sut.getModel(userId);

        assertThat(result).isSameAs(mockModel);
    }

    @Test
    @DisplayName("invalidateCache should clear all cached models")
    void invalidateCacheShouldClearAllModels() throws Exception {
        ChatModel mockModel = mock(ChatModel.class);
        ConcurrentHashMap<String, ChatModel> cache = getCache();
        cache.put("system", mockModel);
        cache.put("user:1", mockModel);

        sut.invalidateCache();

        assertThat(cache).isEmpty();
    }

    @Test
    @DisplayName("invalidateCache should not throw when cache is empty")
    void invalidateCacheShouldNotThrowWhenEmpty() {
        assertThatCode(() -> sut.invalidateCache()).doesNotThrowAnyException();
    }
}
