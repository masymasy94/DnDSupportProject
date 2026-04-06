package com.dndplatform.chat.domain.impl;

import com.dndplatform.chat.domain.DirectConversationCreateService;
import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.domain.repository.ConversationCreateRepository;
import com.dndplatform.chat.domain.repository.ConversationFindDirectRepository;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class DirectConversationCreateServiceImplTest {

    private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2026, 1, 1, 12, 0);
    private final Clock clock = Clock.fixed(FIXED_TIME.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    @Mock
    private ConversationCreateRepository conversationCreateRepository;
    @Mock
    private ConversationFindDirectRepository findDirectRepository;

    private DirectConversationCreateService sut;

    @BeforeEach
    void setUp() {
        sut = new DirectConversationCreateServiceImpl(conversationCreateRepository, findDirectRepository, clock);
    }

    @Test
    void shouldCreateNewDirectConversation(@Random Long createdBy, @Random Long otherUserId, @Random Conversation expected) {
        given(findDirectRepository.findDirectConversation(createdBy, otherUserId)).willReturn(Optional.empty());
        given(conversationCreateRepository.create(any())).willReturn(expected);

        Conversation result = sut.create(createdBy, List.of(otherUserId));

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(findDirectRepository, conversationCreateRepository);
        then(findDirectRepository).should(inOrder).findDirectConversation(createdBy, otherUserId);
        then(conversationCreateRepository).should(inOrder).create(any());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldReturnExistingDirectConversation(@Random Long createdBy, @Random Long otherUserId, @Random Conversation existing) {
        given(findDirectRepository.findDirectConversation(createdBy, otherUserId)).willReturn(Optional.of(existing));

        Conversation result = sut.create(createdBy, List.of(otherUserId));

        assertThat(result).isEqualTo(existing);
        then(findDirectRepository).should().findDirectConversation(createdBy, otherUserId);
    }
}
