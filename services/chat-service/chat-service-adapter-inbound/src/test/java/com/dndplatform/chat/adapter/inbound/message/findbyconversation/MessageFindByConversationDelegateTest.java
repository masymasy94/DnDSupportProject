package com.dndplatform.chat.adapter.inbound.message.findbyconversation;

import com.dndplatform.chat.adapter.inbound.message.mapper.MessageViewModelMapper;
import com.dndplatform.chat.domain.MessageFindByConversationService;
import com.dndplatform.chat.domain.model.Message;
import com.dndplatform.chat.domain.model.PagedResult;
import com.dndplatform.chat.view.model.vm.MessageViewModel;
import com.dndplatform.chat.view.model.vm.PagedMessageViewModel;
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
class MessageFindByConversationDelegateTest {

    @Mock
    private MessageFindByConversationService service;

    @Mock
    private MessageViewModelMapper mapper;

    private MessageFindByConversationDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new MessageFindByConversationDelegate(service, mapper);
    }

    @Test
    void shouldReturnPagedMessages(@Random Long conversationId,
                                   @Random Long userId,
                                   @Random Message m1,
                                   @Random Message m2,
                                   @Random MessageViewModel vm1,
                                   @Random MessageViewModel vm2) {
        int page = 0;
        int pageSize = 50;
        List<Message> messages = List.of(m1, m2);
        PagedResult<Message> pagedResult = new PagedResult<>(messages, page, pageSize, 2L);

        given(service.findByConversationId(conversationId, userId, page, pageSize)).willReturn(pagedResult);
        given(mapper.apply(m1)).willReturn(vm1);
        given(mapper.apply(m2)).willReturn(vm2);

        PagedMessageViewModel result = sut.findByConversation(conversationId, userId, page, pageSize);

        assertThat(result.content()).containsExactly(vm1, vm2);
        assertThat(result.page()).isEqualTo(page);
        assertThat(result.size()).isEqualTo(pageSize);
        assertThat(result.totalElements()).isEqualTo(2L);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).findByConversationId(conversationId, userId, page, pageSize);
        then(mapper).should(inOrder).apply(m1);
        then(mapper).should(inOrder).apply(m2);
    }
}
