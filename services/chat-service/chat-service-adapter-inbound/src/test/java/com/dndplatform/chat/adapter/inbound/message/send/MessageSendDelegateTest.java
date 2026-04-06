package com.dndplatform.chat.adapter.inbound.message.send;

import com.dndplatform.chat.adapter.inbound.message.mapper.MessageViewModelMapper;
import com.dndplatform.chat.domain.MessageSendService;
import com.dndplatform.chat.domain.model.Message;
import com.dndplatform.chat.domain.model.MessageType;
import com.dndplatform.chat.view.model.vm.MessageViewModel;
import com.dndplatform.chat.view.model.vm.SendMessageViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class MessageSendDelegateTest {

    @Mock
    private MessageSendService service;

    @Mock
    private MessageViewModelMapper mapper;

    private MessageSendDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new MessageSendDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToService(@Random Long conversationId,
                                 @Random Long userId,
                                 @Random Message message,
                                 @Random MessageViewModel expected) {
        SendMessageViewModel request = new SendMessageViewModel("Hello party!", "TEXT");

        given(service.send(conversationId, userId, "Hello party!", MessageType.TEXT)).willReturn(message);
        given(mapper.apply(message)).willReturn(expected);

        var actual = sut.send(conversationId, userId, request);

        assertThat(actual).isEqualTo(expected);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).send(conversationId, userId, "Hello party!", MessageType.TEXT);
        then(mapper).should(inOrder).apply(message);
    }
}
