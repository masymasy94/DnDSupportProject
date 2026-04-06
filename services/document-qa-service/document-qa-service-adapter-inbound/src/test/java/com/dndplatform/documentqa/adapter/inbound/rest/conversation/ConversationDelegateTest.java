package com.dndplatform.documentqa.adapter.inbound.rest.conversation;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.documentqa.domain.ConversationDeleteService;
import com.dndplatform.documentqa.domain.ConversationFindService;
import com.dndplatform.documentqa.domain.ConversationHistoryService;
import com.dndplatform.documentqa.domain.model.Conversation;
import com.dndplatform.documentqa.domain.model.ConversationMessage;
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
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ConversationDelegateTest {

    @Mock
    private ConversationFindService findService;

    @Mock
    private ConversationHistoryService historyService;

    @Mock
    private ConversationDeleteService deleteService;

    private ConversationDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationDelegate(findService, historyService, deleteService);
    }

    @Test
    void shouldReturnConversationList(@Random Long userId,
                                      @Random Conversation c1,
                                      @Random Conversation c2) {
        given(findService.findByUserId(userId)).willReturn(List.of(c1, c2));

        List<ConversationViewModel> result = sut.listConversations(userId);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(c1.id());
        assertThat(result.get(1).id()).isEqualTo(c2.id());

        then(findService).should().findByUserId(userId);
    }

    @Test
    void shouldReturnConversationById(@Random Long id, @Random Long userId, @Random Conversation c) {
        given(findService.findById(id, userId)).willReturn(c);

        ConversationViewModel result = sut.getConversation(id, userId);

        assertThat(result.id()).isEqualTo(c.id());
        assertThat(result.userId()).isEqualTo(c.userId());

        then(findService).should().findById(id, userId);
    }

    @Test
    void shouldReturnMessages(@Random Long id,
                              @Random Long userId,
                              @Random ConversationMessage m1,
                              @Random ConversationMessage m2) {
        given(historyService.getHistory(id, userId)).willReturn(List.of(m1, m2));

        List<ConversationMessageViewModel> result = sut.getMessages(id, userId);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(m1.id());
        assertThat(result.get(1).id()).isEqualTo(m2.id());

        then(historyService).should().getHistory(id, userId);
    }

    @Test
    void shouldDelegateDeleteToService(@Random Long id, @Random Long userId) {
        willDoNothing().given(deleteService).delete(id, userId);

        sut.deleteConversation(id, userId);

        then(deleteService).should().delete(id, userId);
    }
}
