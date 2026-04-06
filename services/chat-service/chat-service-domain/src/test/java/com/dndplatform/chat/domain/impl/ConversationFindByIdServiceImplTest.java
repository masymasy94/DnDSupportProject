package com.dndplatform.chat.domain.impl;

import com.dndplatform.chat.domain.ConversationFindByIdService;
import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.domain.model.ConversationParticipant;
import com.dndplatform.chat.domain.model.ParticipantRole;
import com.dndplatform.chat.domain.repository.ConversationFindByIdRepository;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class ConversationFindByIdServiceImplTest {

    @Mock
    private ConversationFindByIdRepository repository;

    private ConversationFindByIdService sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationFindByIdServiceImpl(repository);
    }

    @Test
    void shouldFindConversationByIdWhenUserIsParticipant(@Random Long id, @Random Long userId) {
        Conversation conversation = new Conversation(
                id,
                "Test",
                null,
                1L,
                null,
                null,
                List.of(
                        new ConversationParticipant(1L, id, userId, ParticipantRole.MEMBER, null, null, null)
                )
        );

        given(repository.findById(id)).willReturn(Optional.of(conversation));

        Optional<Conversation> result = sut.findById(id, userId);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(conversation);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findById(id);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldReturnEmptyWhenConversationNotFound(@Random Long id, @Random Long userId) {
        given(repository.findById(id)).willReturn(Optional.empty());

        Optional<Conversation> result = sut.findById(id, userId);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenUserIsNotParticipant(@Random Long id) {
        // userId (999L) is different from participant userId (2L) to test non-membership
        Long userId = 999L;
        Conversation conversation = new Conversation(
                id,
                "Test",
                null,
                1L,
                null,
                null,
                List.of(
                        new ConversationParticipant(1L, id, 2L, ParticipantRole.MEMBER, null, null, null)
                )
        );

        given(repository.findById(id)).willReturn(Optional.of(conversation));

        Optional<Conversation> result = sut.findById(id, userId);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenUserLeftConversation(@Random Long id, @Random Long userId) {
        Conversation conversation = new Conversation(
                id,
                "Test",
                null,
                1L,
                null,
                null,
                List.of(
                        new ConversationParticipant(1L, id, userId, ParticipantRole.MEMBER, null, java.time.LocalDateTime.now(), null)
                )
        );

        given(repository.findById(id)).willReturn(Optional.of(conversation));

        Optional<Conversation> result = sut.findById(id, userId);

        assertThat(result).isEmpty();
    }
}
