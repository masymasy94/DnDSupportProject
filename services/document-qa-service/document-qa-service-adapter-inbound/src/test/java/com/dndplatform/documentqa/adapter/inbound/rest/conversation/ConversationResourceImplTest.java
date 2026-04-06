package com.dndplatform.documentqa.adapter.inbound.rest.conversation;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.documentqa.view.model.vm.ConversationMessageViewModel;
import com.dndplatform.documentqa.view.model.vm.ConversationViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ConversationResourceImplTest {

    @Mock
    private ConversationDelegate delegate;

    private ConversationResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationResourceImpl(delegate);
    }

    @Test
    void shouldDelegateListConversations(@Random ConversationViewModel conversation1,
                                         @Random ConversationViewModel conversation2) {
        Long userId = 1L;
        List<ConversationViewModel> expected = List.of(conversation1, conversation2);
        given(delegate.listConversations(userId)).willReturn(expected);

        List<ConversationViewModel> result = sut.listConversations(userId);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).listConversations(userId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldDelegateGetConversation(@Random ConversationViewModel expected) {
        Long id = 42L;
        Long userId = 1L;
        given(delegate.getConversation(id, userId)).willReturn(expected);

        ConversationViewModel result = sut.getConversation(id, userId);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).getConversation(id, userId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldDelegateGetMessages(@Random ConversationMessageViewModel message1,
                                   @Random ConversationMessageViewModel message2) {
        Long id = 42L;
        Long userId = 1L;
        List<ConversationMessageViewModel> expected = List.of(message1, message2);
        given(delegate.getMessages(id, userId)).willReturn(expected);

        List<ConversationMessageViewModel> result = sut.getMessages(id, userId);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).getMessages(id, userId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldDelegateDeleteConversation() {
        Long id = 42L;
        Long userId = 1L;
        willDoNothing().given(delegate).deleteConversation(id, userId);

        sut.deleteConversation(id, userId);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).deleteConversation(id, userId);
        inOrder.verifyNoMoreInteractions();
    }
}
