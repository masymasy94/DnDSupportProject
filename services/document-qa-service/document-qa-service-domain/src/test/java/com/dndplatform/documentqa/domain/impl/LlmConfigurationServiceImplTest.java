package com.dndplatform.documentqa.domain.impl;

import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.documentqa.domain.model.LlmConfiguration;
import com.dndplatform.documentqa.domain.model.LlmProvider;
import com.dndplatform.documentqa.domain.repository.LlmConfigurationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@DisplayName("LlmConfigurationServiceImpl")
class LlmConfigurationServiceImplTest {

    @Mock
    private LlmConfigurationRepository repository;

    private LlmConfigurationServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new LlmConfigurationServiceImpl(repository);
    }

    @Nested
    @DisplayName("getSystemConfigurations")
    class GetSystemConfigurations {

        @Test
        @DisplayName("should return all system configurations")
        void shouldReturnSystemConfigurations() {
            List<LlmConfiguration> expected = List.of(
                    new LlmConfiguration(1L, null, "System Config 1", LlmProvider.OPENAI, "gpt-4", "https://api.openai.com", "key", "openai", "text-embedding-3-small", true, LocalDateTime.now(), LocalDateTime.now()),
                    new LlmConfiguration(2L, null, "System Config 2", LlmProvider.GROQ, "llama-3", "https://api.groq.com", "key", "groq", "embedding-model", false, LocalDateTime.now(), LocalDateTime.now())
            );
            given(repository.findSystemConfigurations()).willReturn(expected);

            List<LlmConfiguration> result = sut.getSystemConfigurations();

            assertThat(result).isEqualTo(expected);
            then(repository).should().findSystemConfigurations();
            then(repository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("getUserConfigurations")
    class GetUserConfigurations {

        @Test
        @DisplayName("should return user configurations")
        void shouldReturnUserConfigurations() {
            Long userId = 100L;
            List<LlmConfiguration> expected = List.of(
                    new LlmConfiguration(1L, userId, "User Config 1", LlmProvider.ANTHROPIC, "claude-3", "https://api.anthropic.com", "key", "anthropic", "embedding-model", true, LocalDateTime.now(), LocalDateTime.now())
            );
            given(repository.findUserConfigurations(userId)).willReturn(expected);

            List<LlmConfiguration> result = sut.getUserConfigurations(userId);

            assertThat(result).isEqualTo(expected);
            then(repository).should().findUserConfigurations(userId);
            then(repository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("createSystemConfiguration")
    class CreateSystemConfiguration {

        @Test
        @DisplayName("should create system configuration")
        void shouldCreateSystemConfiguration() {
            LlmConfiguration config = new LlmConfiguration(null, null, "New Config", LlmProvider.OPENAI, "gpt-4", "https://api.openai.com", "key", "openai", "embedding", false, null, null);
            LlmConfiguration created = new LlmConfiguration(1L, null, "New Config", LlmProvider.OPENAI, "gpt-4", "https://api.openai.com", "key", "openai", "embedding", false, LocalDateTime.now(), LocalDateTime.now());
            given(repository.create(config)).willReturn(created);

            LlmConfiguration result = sut.createSystemConfiguration(config);

            assertThat(result).isEqualTo(created);
            then(repository).should().create(config);
            then(repository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("createUserConfiguration")
    class CreateUserConfiguration {

        @Test
        @DisplayName("should create user configuration")
        void shouldCreateUserConfiguration() {
            Long userId = 100L;
            LlmConfiguration config = new LlmConfiguration(null, userId, "My Config", LlmProvider.GROQ, "llama-3", "https://api.groq.com", "key", "groq", "embedding", false, null, null);
            LlmConfiguration created = new LlmConfiguration(1L, userId, "My Config", LlmProvider.GROQ, "llama-3", "https://api.groq.com", "key", "groq", "embedding", false, LocalDateTime.now(), LocalDateTime.now());
            given(repository.create(config)).willReturn(created);

            LlmConfiguration result = sut.createUserConfiguration(config);

            assertThat(result).isEqualTo(created);
            then(repository).should().create(config);
            then(repository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("activateSystemConfiguration")
    class ActivateSystemConfiguration {

        @Test
        @DisplayName("should activate system configuration")
        void shouldActivateSystemConfiguration() {
            Long configId = 1L;
            LlmConfiguration config = new LlmConfiguration(configId, null, "Config", LlmProvider.OPENAI, "gpt-4", "url", "key", "openai", "embed", true, LocalDateTime.now(), LocalDateTime.now());
            given(repository.findById(configId)).willReturn(Optional.of(config));

            sut.activateSystemConfiguration(configId);

            then(repository).should().findById(configId);
            then(repository).should().deactivateAllSystem();
            then(repository).should().activate(configId);
            then(repository).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("should throw NotFoundException when config does not exist")
        void shouldThrowNotFoundExceptionWhenConfigDoesNotExist() {
            Long configId = 999L;
            given(repository.findById(configId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> sut.activateSystemConfiguration(configId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Configuration not found: " + configId);

            then(repository).should().findById(configId);
            then(repository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("activateUserConfiguration")
    class ActivateUserConfiguration {

        @Test
        @DisplayName("should activate user configuration when user owns it")
        void shouldActivateUserConfigurationWhenUserOwnsIt() {
            Long configId = 1L;
            Long userId = 100L;
            LlmConfiguration config = new LlmConfiguration(configId, userId, "Config", LlmProvider.OPENAI, "gpt-4", "url", "key", "openai", "embed", true, LocalDateTime.now(), LocalDateTime.now());
            given(repository.findById(configId)).willReturn(Optional.of(config));

            sut.activateUserConfiguration(configId, userId);

            then(repository).should().findById(configId);
            then(repository).should().deactivateAllForUser(userId);
            then(repository).should().activate(configId);
            then(repository).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("should throw NotFoundException when config does not exist")
        void shouldThrowNotFoundExceptionWhenConfigDoesNotExist() {
            Long configId = 999L;
            Long userId = 100L;
            given(repository.findById(configId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> sut.activateUserConfiguration(configId, userId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Configuration not found: " + configId);

            then(repository).should().findById(configId);
            then(repository).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("should throw ForbiddenException when user does not own config")
        void shouldThrowForbiddenExceptionWhenUserDoesNotOwnConfig() {
            Long configId = 1L;
            Long userId = 100L;
            Long otherUserId = 200L;
            LlmConfiguration config = new LlmConfiguration(configId, otherUserId, "Config", LlmProvider.OPENAI, "gpt-4", "url", "key", "openai", "embed", true, LocalDateTime.now(), LocalDateTime.now());
            given(repository.findById(configId)).willReturn(Optional.of(config));

            assertThatThrownBy(() -> sut.activateUserConfiguration(configId, userId))
                    .isInstanceOf(ForbiddenException.class)
                    .hasMessageContaining("Access denied to configuration: " + configId);

            then(repository).should().findById(configId);
            then(repository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("deleteSystemConfiguration")
    class DeleteSystemConfiguration {

        @Test
        @DisplayName("should delete system configuration")
        void shouldDeleteSystemConfiguration() {
            Long configId = 1L;
            LlmConfiguration config = new LlmConfiguration(configId, null, "Config", LlmProvider.OPENAI, "gpt-4", "url", "key", "openai", "embed", false, LocalDateTime.now(), LocalDateTime.now());
            given(repository.findById(configId)).willReturn(Optional.of(config));

            sut.deleteSystemConfiguration(configId);

            then(repository).should().findById(configId);
            then(repository).should().deleteById(configId);
            then(repository).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("should throw NotFoundException when config does not exist")
        void shouldThrowNotFoundExceptionWhenConfigDoesNotExist() {
            Long configId = 999L;
            given(repository.findById(configId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> sut.deleteSystemConfiguration(configId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Configuration not found: " + configId);

            then(repository).should().findById(configId);
            then(repository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("deleteUserConfiguration")
    class DeleteUserConfiguration {

        @Test
        @DisplayName("should delete user configuration when user owns it")
        void shouldDeleteUserConfigurationWhenUserOwnsIt() {
            Long configId = 1L;
            Long userId = 100L;
            LlmConfiguration config = new LlmConfiguration(configId, userId, "Config", LlmProvider.OPENAI, "gpt-4", "url", "key", "openai", "embed", false, LocalDateTime.now(), LocalDateTime.now());
            given(repository.findById(configId)).willReturn(Optional.of(config));

            sut.deleteUserConfiguration(configId, userId);

            then(repository).should().findById(configId);
            then(repository).should().deleteById(configId);
            then(repository).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("should throw NotFoundException when config does not exist")
        void shouldThrowNotFoundExceptionWhenConfigDoesNotExist() {
            Long configId = 999L;
            Long userId = 100L;
            given(repository.findById(configId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> sut.deleteUserConfiguration(configId, userId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Configuration not found: " + configId);

            then(repository).should().findById(configId);
            then(repository).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("should throw ForbiddenException when user does not own config")
        void shouldThrowForbiddenExceptionWhenUserDoesNotOwnConfig() {
            Long configId = 1L;
            Long userId = 100L;
            Long otherUserId = 200L;
            LlmConfiguration config = new LlmConfiguration(configId, otherUserId, "Config", LlmProvider.OPENAI, "gpt-4", "url", "key", "openai", "embed", false, LocalDateTime.now(), LocalDateTime.now());
            given(repository.findById(configId)).willReturn(Optional.of(config));

            assertThatThrownBy(() -> sut.deleteUserConfiguration(configId, userId))
                    .isInstanceOf(ForbiddenException.class)
                    .hasMessageContaining("Access denied to configuration: " + configId);

            then(repository).should().findById(configId);
            then(repository).shouldHaveNoMoreInteractions();
        }
    }
}
