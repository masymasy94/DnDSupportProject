package com.dndplatform.documentqa.adapter.outbound.jpa.repository;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.documentqa.adapter.outbound.jpa.entity.LlmConfigurationEntity;
import com.dndplatform.documentqa.domain.model.LlmConfiguration;
import com.dndplatform.documentqa.domain.model.LlmConfigurationBuilder;
import com.dndplatform.documentqa.domain.model.LlmProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@DisplayName("LlmConfigurationRepositoryJpa")
class LlmConfigurationRepositoryJpaTest {

    @Mock
    private LlmConfigurationPanacheRepository panacheRepository;

    private LlmConfigurationRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new LlmConfigurationRepositoryJpa(panacheRepository);
    }

    private LlmConfigurationEntity buildEntity(Long id, Long userId, String name, boolean active) {
        LlmConfigurationEntity e = new LlmConfigurationEntity();
        e.id = id;
        e.userId = userId;
        e.name = name;
        e.provider = "OPENAI";
        e.modelName = "gpt-4";
        e.baseUrl = "https://api.openai.com";
        e.apiKeyEncrypted = "enc-key";
        e.embeddingProvider = "openai";
        e.embeddingModel = "text-embedding-3-small";
        e.active = active;
        e.createdAt = LocalDateTime.now();
        return e;
    }

    private LlmConfiguration buildDomainConfig(Long id, Long userId, String name, boolean active) {
        return LlmConfigurationBuilder.builder()
                .withId(id)
                .withUserId(userId)
                .withName(name)
                .withProvider(LlmProvider.OPENAI)
                .withModelName("gpt-4")
                .withBaseUrl("https://api.openai.com")
                .withApiKey("enc-key")
                .withEmbeddingProvider("openai")
                .withEmbeddingModel("text-embedding-3-small")
                .withActive(active)
                .withCreatedAt(LocalDateTime.now())
                .withUpdatedAt(null)
                .build();
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("should persist entity and return domain model")
        void shouldPersistEntityAndReturnDomainModel() {
            LlmConfiguration config = buildDomainConfig(null, null, "System GPT-4", false);

            willAnswer(invocation -> {
                LlmConfigurationEntity entity = invocation.getArgument(0);
                entity.id = 1L;
                entity.createdAt = LocalDateTime.now();
                return null;
            }).given(panacheRepository).persist(org.mockito.ArgumentMatchers.<LlmConfigurationEntity>any());

            LlmConfiguration result = sut.create(config);

            assertThat(result.id()).isEqualTo(1L);
            assertThat(result.name()).isEqualTo("System GPT-4");
            assertThat(result.provider()).isEqualTo(LlmProvider.OPENAI);

            then(panacheRepository).should().persist(org.mockito.ArgumentMatchers.<LlmConfigurationEntity>any());
            then(panacheRepository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("should return domain model when found")
        void shouldReturnDomainModelWhenFound() {
            Long id = 7L;
            LlmConfigurationEntity entity = buildEntity(id, null, "GPT Config", true);
            given(panacheRepository.findByIdOptional(id)).willReturn(Optional.of(entity));

            Optional<LlmConfiguration> result = sut.findById(id);

            assertThat(result).isPresent();
            assertThat(result.get().id()).isEqualTo(id);
            assertThat(result.get().name()).isEqualTo("GPT Config");

            then(panacheRepository).should().findByIdOptional(id);
            then(panacheRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            Long id = 999L;
            given(panacheRepository.findByIdOptional(id)).willReturn(Optional.empty());

            Optional<LlmConfiguration> result = sut.findById(id);

            assertThat(result).isEmpty();

            then(panacheRepository).should().findByIdOptional(id);
            then(panacheRepository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("findSystemConfigurations")
    class FindSystemConfigurations {

        @Test
        @DisplayName("should return all system configurations")
        void shouldReturnAllSystemConfigurations() {
            LlmConfigurationEntity e1 = buildEntity(1L, null, "Sys1", true);
            LlmConfigurationEntity e2 = buildEntity(2L, null, "Sys2", false);
            given(panacheRepository.findSystemConfigurations()).willReturn(List.of(e1, e2));

            List<LlmConfiguration> result = sut.findSystemConfigurations();

            assertThat(result).hasSize(2);
            assertThat(result.get(0).name()).isEqualTo("Sys1");
            assertThat(result.get(1).name()).isEqualTo("Sys2");

            then(panacheRepository).should().findSystemConfigurations();
            then(panacheRepository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("findActiveSystemConfiguration")
    class FindActiveSystemConfiguration {

        @Test
        @DisplayName("should return active system configuration when present")
        void shouldReturnActiveSystemConfigurationWhenPresent() {
            LlmConfigurationEntity entity = buildEntity(3L, null, "ActiveSys", true);
            given(panacheRepository.findActiveSystemConfiguration()).willReturn(Optional.of(entity));

            Optional<LlmConfiguration> result = sut.findActiveSystemConfiguration();

            assertThat(result).isPresent();
            assertThat(result.get().active()).isTrue();

            then(panacheRepository).should().findActiveSystemConfiguration();
            then(panacheRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("should return empty when no active system configuration")
        void shouldReturnEmptyWhenNoActiveSystemConfiguration() {
            given(panacheRepository.findActiveSystemConfiguration()).willReturn(Optional.empty());

            Optional<LlmConfiguration> result = sut.findActiveSystemConfiguration();

            assertThat(result).isEmpty();

            then(panacheRepository).should().findActiveSystemConfiguration();
        }
    }

    @Nested
    @DisplayName("activate")
    class Activate {

        @Test
        @DisplayName("should set active flag on found entity")
        void shouldSetActiveFlagOnFoundEntity() {
            Long id = 5L;
            LlmConfigurationEntity entity = buildEntity(id, null, "Config", false);
            given(panacheRepository.findByIdOptional(id)).willReturn(Optional.of(entity));

            sut.activate(id);

            assertThat(entity.active).isTrue();
            assertThat(entity.updatedAt).isNotNull();

            InOrder order = inOrder(panacheRepository);
            order.verify(panacheRepository).findByIdOptional(id);
            order.verifyNoMoreInteractions();
        }

        @Test
        @DisplayName("should throw NotFoundException when entity not found")
        void shouldThrowNotFoundExceptionWhenEntityNotFound() {
            Long id = 999L;
            given(panacheRepository.findByIdOptional(id)).willReturn(Optional.empty());

            assertThatThrownBy(() -> sut.activate(id))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("LLM configuration not found with ID: " + id);

            then(panacheRepository).should().findByIdOptional(id);
            then(panacheRepository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("deleteById")
    class DeleteById {

        @Test
        @DisplayName("should find and delete entity when it exists")
        void shouldFindAndDeleteEntityWhenItExists() {
            Long id = 11L;
            LlmConfigurationEntity entity = buildEntity(id, null, "Config", false);
            given(panacheRepository.findByIdOptional(id)).willReturn(Optional.of(entity));

            sut.deleteById(id);

            InOrder order = inOrder(panacheRepository);
            order.verify(panacheRepository).findByIdOptional(id);
            order.verify(panacheRepository).delete(entity);
            order.verifyNoMoreInteractions();
        }

        @Test
        @DisplayName("should throw NotFoundException when entity not found")
        void shouldThrowNotFoundExceptionWhenEntityNotFound() {
            Long id = 999L;
            given(panacheRepository.findByIdOptional(id)).willReturn(Optional.empty());

            assertThatThrownBy(() -> sut.deleteById(id))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("LLM configuration not found with ID: " + id);

            then(panacheRepository).should().findByIdOptional(id);
            then(panacheRepository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("deactivateAllSystem")
    class DeactivateAllSystem {

        @Test
        @DisplayName("should call update on panache repository for system deactivation")
        void shouldCallUpdateForSystemDeactivation() {
            sut.deactivateAllSystem();

            // Verify update was called once (varargs matching complexity avoided via invocation count)
            then(panacheRepository).should(org.mockito.Mockito.times(1))
                    .update(org.mockito.ArgumentMatchers.anyString(),
                            org.mockito.ArgumentMatchers.any(Object.class));
            then(panacheRepository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("deactivateAllForUser")
    class DeactivateAllForUser {

        @Test
        @DisplayName("should call update on panache repository for user deactivation")
        void shouldCallUpdateForUserDeactivation() {
            Long userId = 100L;

            sut.deactivateAllForUser(userId);

            then(panacheRepository).should(org.mockito.Mockito.times(1))
                    .update(org.mockito.ArgumentMatchers.anyString(),
                            org.mockito.ArgumentMatchers.any(Object.class),
                            org.mockito.ArgumentMatchers.any(Object.class));
            then(panacheRepository).shouldHaveNoMoreInteractions();
        }
    }
}
