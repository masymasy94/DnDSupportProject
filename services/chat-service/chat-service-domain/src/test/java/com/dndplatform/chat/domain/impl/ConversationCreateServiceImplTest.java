package com.dndplatform.chat.domain.impl;

import com.dndplatform.chat.domain.ConversationCreateService;
import com.dndplatform.chat.domain.DirectConversationCreateService;
import com.dndplatform.chat.domain.GroupConversationCreateService;
import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.domain.model.ConversationType;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class ConversationCreateServiceImplTest {

    @Mock
    private DirectConversationCreateService directConversationCreateService;
    @Mock
    private GroupConversationCreateService groupConversationCreateService;

    private ConversationCreateService sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationCreateServiceImpl(directConversationCreateService, groupConversationCreateService);
    }

    @Test
    void shouldCreateDirectConversation(@Random Long createdBy, @Random Long otherUser, @Random Conversation expected) {
        given(directConversationCreateService.create(createdBy, List.of(otherUser))).willReturn(expected);

        Conversation result = sut.create(ConversationType.DIRECT, null, createdBy, List.of(otherUser));

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(directConversationCreateService, groupConversationCreateService);
        then(directConversationCreateService).should(inOrder).create(createdBy, List.of(otherUser));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldCreateGroupConversation(@Random String name, @Random Long createdBy, @Random Conversation expected) {
        List<Long> participants = List.of(2L, 3L);

        given(groupConversationCreateService.create(name, createdBy, participants)).willReturn(expected);

        Conversation result = sut.create(ConversationType.GROUP, name, createdBy, participants);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(directConversationCreateService, groupConversationCreateService);
        then(groupConversationCreateService).should(inOrder).create(name, createdBy, participants);
        inOrder.verifyNoMoreInteractions();
    }
}
