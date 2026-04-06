package com.dndplatform.documentqa.adapter.outbound.jpa.repository;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.documentqa.adapter.outbound.jpa.entity.ConversationEntity;
import com.dndplatform.documentqa.adapter.outbound.jpa.entity.ConversationMessageEntity;
import com.dndplatform.documentqa.domain.model.ConversationMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@DisplayName("ConversationMessageCreateRepositoryJpa")
class ConversationMessageCreateRepositoryJpaTest {

    @Mock
    private ConversationPanacheRepository conversationPanache;

    @Mock
    private ConversationMessagePanacheRepository messagePanache;

    private ConversationMessageCreateRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationMessageCreateRepositoryJpa(conversationPanache, messagePanache);
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("should create message and return domain model when conversation exists")
        void shouldCreateMessageWhenConversationExists() {
            Long conversationId = 10L;
            String role = "USER";
            String content = "What spells does the wizard know?";

            ConversationEntity conversationEntity = new ConversationEntity();
            conversationEntity.id = conversationId;
            given(conversationPanache.findByIdOptional(conversationId)).willReturn(Optional.of(conversationEntity));

            willAnswer(invocation -> {
                ConversationMessageEntity entity = invocation.getArgument(0);
                entity.id = 55L;
                entity.createdAt = LocalDateTime.now();
                return null;
            }).given(messagePanache).persist(org.mockito.ArgumentMatchers.<ConversationMessageEntity>any());

            ConversationMessage result = sut.create(conversationId, role, content);

            InOrder order = inOrder(conversationPanache, messagePanache);
            order.verify(conversationPanache).findByIdOptional(conversationId);
            order.verify(messagePanache).persist(org.mockito.ArgumentMatchers.<ConversationMessageEntity>any());
            order.verifyNoMoreInteractions();

            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(55L);
            assertThat(result.conversationId()).isEqualTo(conversationId);
            assertThat(result.role()).isEqualTo(role);
            assertThat(result.content()).isEqualTo(content);
        }

        @Test
        @DisplayName("should throw NotFoundException when conversation not found")
        void shouldThrowNotFoundExceptionWhenConversationNotFound() {
            Long conversationId = 999L;
            given(conversationPanache.findByIdOptional(conversationId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> sut.create(conversationId, "USER", "Hello"))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Conversation not found with ID: " + conversationId);

            then(conversationPanache).should().findByIdOptional(conversationId);
            then(messagePanache).shouldHaveNoInteractions();
        }
    }
}
