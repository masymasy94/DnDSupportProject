package com.dndplatform.chat.adapter.inbound.message.findbyconversation;

import com.dndplatform.chat.view.model.MessageFindByConversationResource;
import com.dndplatform.chat.view.model.vm.PagedMessageViewModel;
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
class MessageFindByConversationResourceImplTest {

    @Mock
    private MessageFindByConversationResource delegate;

    private MessageFindByConversationResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new MessageFindByConversationResourceImpl(delegate);
    }

    @Test
    void shouldDelegateFindByConversation(
            @Random Long conversationId,
            @Random Long userId,
            @Random Integer page,
            @Random Integer pageSize,
            @Random PagedMessageViewModel expected) {
        given(delegate.findByConversation(conversationId, userId, page, pageSize)).willReturn(expected);

        var result = sut.findByConversation(conversationId, userId, page, pageSize);

        assertThat(result).isEqualTo(expected);
        then(delegate).should().findByConversation(conversationId, userId, page, pageSize);
        then(delegate).shouldHaveNoMoreInteractions();
    }
}
