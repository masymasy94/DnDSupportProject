package com.dndplatform.chat.adapter.inbound.conversation.findbyuser;

import com.dndplatform.chat.adapter.inbound.conversation.mapper.ConversationViewModelMapper;
import com.dndplatform.chat.domain.ConversationFindByUserService;
import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.view.model.vm.ConversationViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ConversationFindByUserDelegateTest {

    @Mock
    private ConversationFindByUserService service;

    @Mock
    private ConversationViewModelMapper mapper;

    private ConversationFindByUserDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationFindByUserDelegate(service, mapper);
    }

    @Test
    void shouldReturnMappedConversations(@Random long userId,
                                         @Random Conversation c1,
                                         @Random Conversation c2,
                                         @Random ConversationViewModel vm1,
                                         @Random ConversationViewModel vm2) {
        List<Conversation> conversations = List.of(c1, c2);
        given(service.findByUserId(userId)).willReturn(conversations);
        given(mapper.apply(c1)).willReturn(vm1);
        given(mapper.apply(c2)).willReturn(vm2);

        var result = sut.findByUser(userId);

        assertThat(result).containsExactly(vm1, vm2);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).findByUserId(userId);
        then(mapper).should(inOrder).apply(c1);
        then(mapper).should(inOrder).apply(c2);
    }
}
