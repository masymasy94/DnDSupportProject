package com.dndplatform.documentqa.domain.impl;

import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.documentqa.domain.model.Conversation;
import com.dndplatform.documentqa.domain.repository.ConversationFindRepository;
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
@DisplayName("ConversationFindServiceImpl")
class ConversationFindServiceImplTest {

    @Mock
    private ConversationFindRepository repository;

    private ConversationFindServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationFindServiceImpl(repository);
    }

    @Nested
    @DisplayName("findByUserId")
    class FindByUserId {

        @Test
        @DisplayName("should return all conversations for user")
        void shouldReturnAllConversationsForUser() {
            Long userId = 100L;
            List<Conversation> expected = List.of(
                    new Conversation(1L, userId, null, "First", LocalDateTime.now(), LocalDateTime.now()),
                    new Conversation(2L, userId, null, "Second", LocalDateTime.now(), LocalDateTime.now())
            );
            given(repository.findByUserId(userId)).willReturn(expected);

            List<Conversation> result = sut.findByUserId(userId);

            assertThat(result).isEqualTo(expected);
            then(repository).should().findByUserId(userId);
            then(repository).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("should return empty list when user has no conversations")
        void shouldReturnEmptyListWhenUserHasNoConversations() {
            Long userId = 100L;
            given(repository.findByUserId(userId)).willReturn(List.of());

            List<Conversation> result = sut.findByUserId(userId);

            assertThat(result).isEmpty();
            then(repository).should().findByUserId(userId);
            then(repository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("should return conversation when exists and user has access")
        void shouldReturnConversationWhenExistsAndUserHasAccess() {
            Long id = 1L;
            Long userId = 100L;
            Conversation expected = new Conversation(id, userId, null, "Test", LocalDateTime.now(), LocalDateTime.now());
            given(repository.findById(id)).willReturn(Optional.of(expected));

            Conversation result = sut.findById(id, userId);

            assertThat(result).isEqualTo(expected);
            then(repository).should().findById(id);
            then(repository).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("should throw NotFoundException when conversation does not exist")
        void shouldThrowNotFoundExceptionWhenConversationDoesNotExist() {
            Long id = 999L;
            Long userId = 100L;
            given(repository.findById(id)).willReturn(Optional.empty());

            assertThatThrownBy(() -> sut.findById(id, userId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Conversation not found: " + id);

            then(repository).should().findById(id);
            then(repository).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("should throw ForbiddenException when user does not own conversation")
        void shouldThrowForbiddenExceptionWhenUserDoesNotOwnConversation() {
            Long id = 1L;
            Long userId = 100L;
            Long otherUserId = 200L;
            Conversation conversation = new Conversation(id, otherUserId, null, "Test", LocalDateTime.now(), LocalDateTime.now());
            given(repository.findById(id)).willReturn(Optional.of(conversation));

            assertThatThrownBy(() -> sut.findById(id, userId))
                    .isInstanceOf(ForbiddenException.class)
                    .hasMessageContaining("Access denied to conversation: " + id);

            then(repository).should().findById(id);
            then(repository).shouldHaveNoMoreInteractions();
        }
    }
}
