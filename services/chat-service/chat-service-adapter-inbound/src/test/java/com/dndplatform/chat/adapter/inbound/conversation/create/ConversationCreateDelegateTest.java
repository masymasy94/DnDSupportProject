package com.dndplatform.chat.adapter.inbound.conversation.create;

import com.dndplatform.chat.adapter.inbound.conversation.mapper.ConversationViewModelMapper;
import com.dndplatform.chat.domain.ConversationCreateService;
import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.domain.model.ConversationType;
import com.dndplatform.chat.view.model.vm.ConversationViewModel;
import com.dndplatform.chat.view.model.vm.CreateConversationViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ConversationCreateDelegateTest {

    @Mock
    private ConversationCreateService conversationCreateService;

    @Mock
    private ConversationViewModelMapper mapper;

    private ConversationCreateDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationCreateDelegate(conversationCreateService, mapper);
    }

    @Test
    void shouldDelegateToService(@Random Long userId,
                                 @Random Conversation conversation,
                                 @Random ConversationViewModel expected) {
        List<Long> participantIds = List.of(2L, 3L);
        CreateConversationViewModel request = new CreateConversationViewModel("GROUP", "Party Chat", participantIds);

        given(conversationCreateService.create(ConversationType.GROUP, "Party Chat", userId, participantIds))
                .willReturn(conversation);
        given(mapper.apply(conversation)).willReturn(expected);

        var actual = sut.create(userId, request);

        assertThat(actual).isEqualTo(expected);

        var inOrder = inOrder(conversationCreateService, mapper);
        then(conversationCreateService).should(inOrder).create(ConversationType.GROUP, "Party Chat", userId, participantIds);
        then(mapper).should(inOrder).apply(conversation);
    }

    @Test
    void shouldCreateDirectConversationWithValidParticipants(@Random Long userId,
                                                              @Random Conversation conversation,
                                                              @Random ConversationViewModel expected) {
        List<Long> participantIds = List.of(2L);
        CreateConversationViewModel request = new CreateConversationViewModel("DIRECT", null, participantIds);

        given(conversationCreateService.create(ConversationType.DIRECT, null, userId, participantIds))
                .willReturn(conversation);
        given(mapper.apply(conversation)).willReturn(expected);

        var actual = sut.create(userId, request);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldThrowWhenDirectConversationHasNullParticipants(@Random Long userId) {
        CreateConversationViewModel request = new CreateConversationViewModel("DIRECT", null, null);

        assertThatThrownBy(() -> sut.create(userId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least one other participant");
    }

    @Test
    void shouldThrowWhenDirectConversationHasEmptyParticipants(@Random Long userId) {
        CreateConversationViewModel request = new CreateConversationViewModel("DIRECT", null, List.of());

        assertThatThrownBy(() -> sut.create(userId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least one other participant");
    }
}
