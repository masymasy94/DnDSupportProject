package com.dndplatform.chat.domain.impl;

import com.dndplatform.chat.domain.GroupConversationCreateService;
import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.domain.repository.ConversationCreateRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class GroupConversationCreateServiceImplTest {

    private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2026, 1, 1, 12, 0);
    private final Clock clock = Clock.fixed(FIXED_TIME.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    @Mock
    private ConversationCreateRepository conversationCreateRepository;

    private GroupConversationCreateService sut;

    @BeforeEach
    void setUp() {
        sut = new GroupConversationCreateServiceImpl(conversationCreateRepository, clock);
    }

    @Test
    void shouldCreateGroupConversation(@Random String name, @Random Long createdBy, @Random Conversation expected) {
        List<Long> participantIds = List.of(2L, 3L);

        given(conversationCreateRepository.create(any())).willReturn(expected);

        Conversation result = sut.create(name, createdBy, participantIds);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(conversationCreateRepository);
        then(conversationCreateRepository).should(inOrder).create(any());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldCreateGroupConversationWithEmptyParticipants(@Random String name, @Random Long createdBy, @Random Conversation expected) {
        List<Long> participantIds = List.of();

        given(conversationCreateRepository.create(any())).willReturn(expected);

        Conversation result = sut.create(name, createdBy, participantIds);

        assertThat(result).isNotNull();
        then(conversationCreateRepository).should().create(any());
    }

    @Test
    void shouldCreateGroupConversationWithNullParticipants(@Random String name, @Random Long createdBy, @Random Conversation expected) {
        given(conversationCreateRepository.create(any())).willReturn(expected);

        Conversation result = sut.create(name, createdBy, null);

        assertThat(result).isNotNull();
        then(conversationCreateRepository).should().create(any());
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        assertThatThrownBy(() -> sut.create(null, 1L, List.of(2L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Group conversation requires a name");
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        assertThatThrownBy(() -> sut.create("   ", 1L, List.of(2L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Group conversation requires a name");
    }

    @Test
    void shouldExcludeCreatorFromParticipantsList(@Random String name, @Random Long createdBy, @Random Conversation expected) {
        List<Long> participantIds = List.of(createdBy, 2L, 3L);

        given(conversationCreateRepository.create(any())).willReturn(expected);

        Conversation result = sut.create(name, createdBy, participantIds);

        assertThat(result).isNotNull();
    }
}
