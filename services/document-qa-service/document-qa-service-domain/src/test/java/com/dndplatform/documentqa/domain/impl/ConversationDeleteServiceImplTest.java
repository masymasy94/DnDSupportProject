package com.dndplatform.documentqa.domain.impl;

import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.documentqa.domain.model.Conversation;
import com.dndplatform.documentqa.domain.repository.ConversationDeleteRepository;
import com.dndplatform.documentqa.domain.repository.ConversationFindRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConversationDeleteServiceImpl")
class ConversationDeleteServiceImplTest {

    @Mock
    private ConversationFindRepository findRepository;

    @Mock
    private ConversationDeleteRepository deleteRepository;

    private ConversationDeleteServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationDeleteServiceImpl(findRepository, deleteRepository);
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("should delete conversation when exists and user has access")
        void shouldDeleteConversationWhenExistsAndUserHasAccess() {
            Long id = 1L;
            Long userId = 100L;
            Conversation conversation = new Conversation(id, userId, null, "Test", LocalDateTime.now(), LocalDateTime.now());
            given(findRepository.findById(id)).willReturn(Optional.of(conversation));

            sut.delete(id, userId);

            then(findRepository).should().findById(id);
            then(deleteRepository).should().deleteById(id);
            then(findRepository).shouldHaveNoMoreInteractions();
            then(deleteRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("should throw NotFoundException when conversation does not exist")
        void shouldThrowNotFoundExceptionWhenConversationDoesNotExist() {
            Long id = 999L;
            Long userId = 100L;
            given(findRepository.findById(id)).willReturn(Optional.empty());

            assertThatThrownBy(() -> sut.delete(id, userId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Conversation not found: " + id);

            then(findRepository).should().findById(id);
            then(deleteRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("should throw ForbiddenException when user does not own conversation")
        void shouldThrowForbiddenExceptionWhenUserDoesNotOwnConversation() {
            Long id = 1L;
            Long userId = 100L;
            Long otherUserId = 200L;
            Conversation conversation = new Conversation(id, otherUserId, null, "Test", LocalDateTime.now(), LocalDateTime.now());
            given(findRepository.findById(id)).willReturn(Optional.of(conversation));

            assertThatThrownBy(() -> sut.delete(id, userId))
                    .isInstanceOf(ForbiddenException.class)
                    .hasMessageContaining("Access denied to conversation: " + id);

            then(findRepository).should().findById(id);
            then(deleteRepository).shouldHaveNoMoreInteractions();
        }
    }
}
