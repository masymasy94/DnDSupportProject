package com.dndplatform.documentqa.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.documentqa.adapter.outbound.jpa.entity.ConversationEntity;
import com.dndplatform.documentqa.domain.model.Conversation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@DisplayName("ConversationCreateRepositoryJpa")
class ConversationCreateRepositoryJpaTest {

    @Mock
    private ConversationPanacheRepository panacheRepository;

    private ConversationCreateRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationCreateRepositoryJpa(panacheRepository);
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("should persist entity and return domain model")
        void shouldPersistEntityAndReturnDomainModel() {
            Long userId = 42L;
            Long campaignId = 7L;
            String title = "The Fellowship Journey";

            willAnswer(invocation -> {
                ConversationEntity entity = invocation.getArgument(0);
                entity.id = 99L;
                entity.createdAt = LocalDateTime.now();
                return null;
            }).given(panacheRepository).persist(org.mockito.ArgumentMatchers.<ConversationEntity>any());

            Conversation result = sut.create(userId, campaignId, title);

            InOrder order = inOrder(panacheRepository);
            order.verify(panacheRepository).persist(org.mockito.ArgumentMatchers.<ConversationEntity>any());
            order.verifyNoMoreInteractions();

            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(99L);
            assertThat(result.userId()).isEqualTo(userId);
            assertThat(result.campaignId()).isEqualTo(campaignId);
            assertThat(result.title()).isEqualTo(title);
        }

        @Test
        @DisplayName("should map entity fields correctly to domain model")
        void shouldMapEntityFieldsCorrectlyToDomainModel() {
            Long userId = 1L;
            Long campaignId = null;
            String title = "Solo Adventure";
            LocalDateTime now = LocalDateTime.now();

            willAnswer(invocation -> {
                ConversationEntity entity = invocation.getArgument(0);
                entity.id = 10L;
                entity.createdAt = now;
                entity.updatedAt = now;
                return null;
            }).given(panacheRepository).persist(org.mockito.ArgumentMatchers.<ConversationEntity>any());

            Conversation result = sut.create(userId, campaignId, title);

            assertThat(result.userId()).isEqualTo(userId);
            assertThat(result.campaignId()).isNull();
            assertThat(result.title()).isEqualTo(title);
            assertThat(result.createdAt()).isEqualTo(now);
            assertThat(result.updatedAt()).isEqualTo(now);

            then(panacheRepository).should().persist(org.mockito.ArgumentMatchers.<ConversationEntity>any());
            then(panacheRepository).shouldHaveNoMoreInteractions();
        }
    }
}
