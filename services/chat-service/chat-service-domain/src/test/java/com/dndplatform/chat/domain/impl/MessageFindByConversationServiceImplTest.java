package com.dndplatform.chat.domain.impl;

import com.dndplatform.chat.domain.MessageFindByConversationService;
import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.domain.model.ConversationParticipant;
import com.dndplatform.chat.domain.model.Message;
import com.dndplatform.chat.domain.model.PagedResult;
import com.dndplatform.chat.domain.model.ParticipantRole;
import com.dndplatform.chat.domain.repository.ConversationFindByIdRepository;
import com.dndplatform.chat.domain.repository.MessageFindByConversationRepository;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class MessageFindByConversationServiceImplTest {

    @Mock
    private MessageFindByConversationRepository messageRepository;
    @Mock
    private ConversationFindByIdRepository conversationRepository;

    private MessageFindByConversationService sut;

    @BeforeEach
    void setUp() {
        sut = new MessageFindByConversationServiceImpl(messageRepository, conversationRepository);
    }

    @Test
    void shouldFindMessagesByConversationId(@Random Long conversationId, @Random Long userId) {
        Conversation conversation = new Conversation(
                conversationId,
                "Test",
                null,
                1L,
                null,
                null,
                List.of(
                        new ConversationParticipant(1L, conversationId, userId, ParticipantRole.MEMBER, null, null, null)
                )
        );
        PagedResult<Message> expected = new PagedResult<>(List.of(new Message(1L, conversationId, 1L, "Hello", null, null, null, null)), 0, 10, 1);

        given(conversationRepository.findById(conversationId)).willReturn(Optional.of(conversation));
        given(messageRepository.findByConversationId(conversationId, 0, 10)).willReturn(expected);

        PagedResult<Message> result = sut.findByConversationId(conversationId, userId, 0, 10);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(conversationRepository, messageRepository);
        then(conversationRepository).should(inOrder).findById(conversationId);
        then(messageRepository).should(inOrder).findByConversationId(conversationId, 0, 10);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldApplyDefaultPagination(@Random Long conversationId, @Random Long userId) {
        Conversation conversation = new Conversation(
                conversationId,
                "Test",
                null,
                1L,
                null,
                null,
                List.of(
                        new ConversationParticipant(1L, conversationId, userId, ParticipantRole.MEMBER, null, null, null)
                )
        );
        PagedResult<Message> expected = new PagedResult<>(List.of(), 0, 50, 0);

        given(conversationRepository.findById(conversationId)).willReturn(Optional.of(conversation));
        given(messageRepository.findByConversationId(conversationId, 0, 50)).willReturn(expected);

        PagedResult<Message> result = sut.findByConversationId(conversationId, userId, -1, 0);

        assertThat(result.page()).isEqualTo(0);
        assertThat(result.size()).isEqualTo(50);
    }

    @Test
    void shouldThrowExceptionWhenConversationNotFound(@Random Long conversationId, @Random Long userId) {
        given(conversationRepository.findById(conversationId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.findByConversationId(conversationId, userId, 0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Conversation not found");
    }

    @Test
    void shouldThrowExceptionWhenUserNotParticipant(@Random Long conversationId) {
        // userId (999L) is different from participant userId (2L) to test non-membership
        Long userId = 999L;
        Conversation conversation = new Conversation(
                conversationId,
                "Test",
                null,
                1L,
                null,
                null,
                List.of(
                        new ConversationParticipant(1L, conversationId, 2L, ParticipantRole.MEMBER, null, null, null)
                )
        );

        given(conversationRepository.findById(conversationId)).willReturn(Optional.of(conversation));

        assertThatThrownBy(() -> sut.findByConversationId(conversationId, userId, 0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not a participant");
    }

    @Test
    void shouldThrowExceptionWhenUserLeftConversation(@Random Long conversationId, @Random Long userId) {
        Conversation conversation = new Conversation(
                conversationId,
                "Test",
                null,
                1L,
                null,
                null,
                List.of(
                        new ConversationParticipant(1L, conversationId, userId, ParticipantRole.MEMBER, null, java.time.LocalDateTime.now(), null)
                )
        );

        given(conversationRepository.findById(conversationId)).willReturn(Optional.of(conversation));

        assertThatThrownBy(() -> sut.findByConversationId(conversationId, userId, 0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not a participant");
    }
}
