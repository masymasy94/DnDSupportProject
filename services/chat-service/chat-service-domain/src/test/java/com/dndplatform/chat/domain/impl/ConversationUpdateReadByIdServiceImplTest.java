package com.dndplatform.chat.domain.impl;

import com.dndplatform.chat.domain.ConversationUpdateReadByIdService;
import com.dndplatform.chat.domain.repository.ParticipantExistsRepository;
import com.dndplatform.chat.domain.repository.ParticipantUpdateLastReadRepository;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.Mockito.doNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class ConversationUpdateReadByIdServiceImplTest {

    @Mock
    private ParticipantUpdateLastReadRepository participantUpdateLastReadRepository;
    @Mock
    private ParticipantExistsRepository participantExistsRepository;

    private ConversationUpdateReadByIdService sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationUpdateReadByIdServiceImpl(participantUpdateLastReadRepository, participantExistsRepository);
    }

    @Test
    void shouldUpdateReadStatus(@Random Long conversationId, @Random Long userId) {
        given(participantExistsRepository.existsByConversationIdAndUserId(conversationId, userId)).willReturn(true);
        doNothing().when(participantUpdateLastReadRepository).updateLastReadAt(conversationId, userId);

        sut.updateReadById(conversationId, userId);

        var inOrder = inOrder(participantExistsRepository, participantUpdateLastReadRepository);
        then(participantExistsRepository).should(inOrder).existsByConversationIdAndUserId(conversationId, userId);
        then(participantUpdateLastReadRepository).should(inOrder).updateLastReadAt(conversationId, userId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowExceptionWhenUserNotParticipant(@Random Long conversationId, @Random Long userId) {
        given(participantExistsRepository.existsByConversationIdAndUserId(conversationId, userId)).willReturn(false);

        assertThatThrownBy(() -> sut.updateReadById(conversationId, userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not a participant");
    }
}
