package com.dndplatform.documentqa.domain.impl;

import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.documentqa.domain.model.Conversation;
import com.dndplatform.documentqa.domain.model.ConversationMessage;
import com.dndplatform.documentqa.domain.repository.ConversationFindRepository;
import com.dndplatform.documentqa.domain.repository.ConversationMessageFindRepository;
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
@DisplayName("ConversationHistoryServiceImpl")
class ConversationHistoryServiceImplTest {

    @Mock
    private ConversationFindRepository conversationRepository;

    @Mock
    private ConversationMessageFindRepository messageRepository;

    private ConversationHistoryServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationHistoryServiceImpl(conversationRepository, messageRepository);
    }

    @Nested
    @DisplayName("getHistory")
    class GetHistory {

        @Test
        @DisplayName("should return conversation messages when conversation exists and user has access")
        void shouldReturnMessagesWhenConversationExistsAndUserHasAccess() {
            Long conversationId = 1L;
            Long userId = 100L;
            Conversation conversation = new Conversation(
                    conversationId,
                    userId,
                    null,
                    "Test Conversation",
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            List<ConversationMessage> messages = List.of(
                    new ConversationMessage(1L, conversationId, "USER", "Hello", LocalDateTime.now()),
                    new ConversationMessage(2L, conversationId, "ASSISTANT", "Hi there!", LocalDateTime.now())
            );

            given(conversationRepository.findById(conversationId)).willReturn(Optional.of(conversation));
            given(messageRepository.findByConversationId(conversationId)).willReturn(messages);

            List<ConversationMessage> result = sut.getHistory(conversationId, userId);

            assertThat(result).isEqualTo(messages);
            then(conversationRepository).should().findById(conversationId);
            then(messageRepository).should().findByConversationId(conversationId);
            then(conversationRepository).shouldHaveNoMoreInteractions();
            then(messageRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("should throw NotFoundException when conversation does not exist")
        void shouldThrowNotFoundExceptionWhenConversationDoesNotExist() {
            Long conversationId = 999L;
            Long userId = 100L;
            given(conversationRepository.findById(conversationId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> sut.getHistory(conversationId, userId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Conversation not found: " + conversationId);

            then(conversationRepository).should().findById(conversationId);
            then(messageRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("should throw ForbiddenException when user does not own conversation")
        void shouldThrowForbiddenExceptionWhenUserDoesNotOwnConversation() {
            Long conversationId = 1L;
            Long userId = 100L;
            Long otherUserId = 200L;
            Conversation conversation = new Conversation(
                    conversationId,
                    otherUserId,
                    null,
                    "Test Conversation",
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            given(conversationRepository.findById(conversationId)).willReturn(Optional.of(conversation));

            assertThatThrownBy(() -> sut.getHistory(conversationId, userId))
                    .isInstanceOf(ForbiddenException.class)
                    .hasMessageContaining("Access denied to conversation: " + conversationId);

            then(conversationRepository).should().findById(conversationId);
            then(messageRepository).shouldHaveNoMoreInteractions();
        }
    }
}
