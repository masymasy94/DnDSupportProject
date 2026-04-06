package com.dndplatform.documentqa.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.documentqa.adapter.outbound.jpa.entity.ConversationEntity;
import com.dndplatform.documentqa.adapter.outbound.jpa.entity.ConversationMessageEntity;
import com.dndplatform.documentqa.domain.model.ConversationMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@DisplayName("ConversationMessageFindRepositoryJpa")
class ConversationMessageFindRepositoryJpaTest {

    @Mock
    private ConversationMessagePanacheRepository panacheRepository;

    private ConversationMessageFindRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationMessageFindRepositoryJpa(panacheRepository);
    }

    private ConversationMessageEntity buildEntity(Long id, Long conversationId, String role, String content) {
        ConversationEntity conv = new ConversationEntity();
        conv.id = conversationId;

        ConversationMessageEntity e = new ConversationMessageEntity();
        e.id = id;
        e.conversation = conv;
        e.role = role;
        e.content = content;
        e.createdAt = LocalDateTime.now();
        return e;
    }

    @Nested
    @DisplayName("findByConversationId")
    class FindByConversationId {

        @Test
        @DisplayName("should return messages sorted by createdAt for conversation")
        void shouldReturnMessagesForConversation() {
            Long conversationId = 20L;
            ConversationMessageEntity msg1 = buildEntity(1L, conversationId, "USER", "Hello");
            ConversationMessageEntity msg2 = buildEntity(2L, conversationId, "ASSISTANT", "Hi there");
            given(panacheRepository.findByConversationId(conversationId)).willReturn(List.of(msg1, msg2));

            List<ConversationMessage> result = sut.findByConversationId(conversationId);

            assertThat(result).hasSize(2);
            assertThat(result.get(0).id()).isEqualTo(1L);
            assertThat(result.get(0).role()).isEqualTo("USER");
            assertThat(result.get(0).content()).isEqualTo("Hello");
            assertThat(result.get(0).conversationId()).isEqualTo(conversationId);
            assertThat(result.get(1).role()).isEqualTo("ASSISTANT");

            then(panacheRepository).should().findByConversationId(conversationId);
            then(panacheRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("should return empty list when conversation has no messages")
        void shouldReturnEmptyListWhenNoMessages() {
            Long conversationId = 777L;
            given(panacheRepository.findByConversationId(conversationId)).willReturn(List.of());

            List<ConversationMessage> result = sut.findByConversationId(conversationId);

            assertThat(result).isEmpty();

            then(panacheRepository).should().findByConversationId(conversationId);
            then(panacheRepository).shouldHaveNoMoreInteractions();
        }
    }
}
