package com.dndplatform.chat.adapter.inbound.message.send;

import com.dndplatform.chat.view.model.MessageSendResource;
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

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class MessageSendResourceImplTest {

    @Mock
    private MessageSendResource delegate;

    private MessageSendResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new MessageSendResourceImpl(delegate);
    }

    @Test
    void shouldDelegateSend(@Random Long conversationId, @Random Long userId, @Random SendMessageViewModel request, @Random MessageViewModel expected) {
        given(delegate.send(conversationId, userId, request)).willReturn(expected);

        var result = sut.send(conversationId, userId, request);

        assertThat(result).isEqualTo(expected);
        then(delegate).should().send(conversationId, userId, request);
        then(delegate).shouldHaveNoMoreInteractions();
    }
}
