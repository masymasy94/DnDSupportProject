package com.dndplatform.chat.domain.impl;

import com.dndplatform.chat.domain.MessageSendService;
import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.domain.model.ConversationParticipant;
import com.dndplatform.chat.domain.model.Message;
import com.dndplatform.chat.domain.model.MessageType;
import com.dndplatform.chat.domain.model.ParticipantRole;
import com.dndplatform.chat.domain.repository.ConversationFindByIdRepository;
import com.dndplatform.chat.domain.repository.MessageCreateRepository;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class MessageSendServiceImplTest {

    private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2026, 1, 1, 12, 0);
    private final Clock clock = Clock.fixed(FIXED_TIME.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    @Mock
    private MessageCreateRepository messageRepository;
    @Mock
    private ConversationFindByIdRepository conversationRepository;

    private MessageSendService sut;

    @BeforeEach
    void setUp() {
        sut = new MessageSendServiceImpl(messageRepository, conversationRepository, clock);
    }

    @Test
    void shouldSendMessage(@Random Long conversationId, @Random Long senderId, @Random String content, @Random Message expected) {
        Conversation conversation = new Conversation(
                conversationId,
                "Test",
                null,
                1L,
                null,
                null,
                List.of(
                        new ConversationParticipant(1L, conversationId, senderId, ParticipantRole.MEMBER, null, null, null)
                )
        );

        given(conversationRepository.findById(conversationId)).willReturn(Optional.of(conversation));
        given(messageRepository.create(any())).willReturn(expected);

        Message result = sut.send(conversationId, senderId, content, MessageType.TEXT);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(conversationRepository, messageRepository);
        then(conversationRepository).should(inOrder).findById(conversationId);
        then(messageRepository).should(inOrder).create(any());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldSendMessageWithDefaultType(@Random Long conversationId, @Random Long senderId, @Random String content) {
        Conversation conversation = new Conversation(
                conversationId,
                "Test",
                null,
                1L,
                null,
                null,
                List.of(
                        new ConversationParticipant(1L, conversationId, senderId, ParticipantRole.MEMBER, null, null, null)
                )
        );
        Message expected = new Message(1L, conversationId, senderId, content, MessageType.TEXT, null, null, null);

        given(conversationRepository.findById(conversationId)).willReturn(Optional.of(conversation));
        given(messageRepository.create(any())).willReturn(expected);

        Message result = sut.send(conversationId, senderId, content, null);

        assertThat(result.messageType()).isEqualTo(MessageType.TEXT);
    }

    @Test
    void shouldThrowExceptionWhenConversationNotFound(@Random Long conversationId, @Random Long senderId) {
        given(conversationRepository.findById(conversationId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.send(conversationId, senderId, "Hello", MessageType.TEXT))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Conversation not found");
    }

    @Test
    void shouldThrowExceptionWhenUserNotParticipant(@Random Long conversationId) {
        // senderId (999L) is different from participant userId (2L) to test non-membership
        Long senderId = 999L;
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

        assertThatThrownBy(() -> sut.send(conversationId, senderId, "Hello", MessageType.TEXT))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not a participant");
    }

    @Test
    void shouldThrowExceptionWhenUserLeftConversation(@Random Long conversationId, @Random Long senderId) {
        Conversation conversation = new Conversation(
                conversationId,
                "Test",
                null,
                1L,
                null,
                null,
                List.of(
                        new ConversationParticipant(1L, conversationId, senderId, ParticipantRole.MEMBER, null, FIXED_TIME, null)
                )
        );

        given(conversationRepository.findById(conversationId)).willReturn(Optional.of(conversation));

        assertThatThrownBy(() -> sut.send(conversationId, senderId, "Hello", MessageType.TEXT))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not a participant");
    }
}
