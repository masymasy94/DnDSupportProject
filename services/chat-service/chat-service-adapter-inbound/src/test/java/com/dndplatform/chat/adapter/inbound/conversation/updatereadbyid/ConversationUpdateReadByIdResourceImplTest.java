package com.dndplatform.chat.adapter.inbound.conversation.updatereadbyid;

import com.dndplatform.chat.view.model.ConversationUpdateReadByIdResource;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ConversationUpdateReadByIdResourceImplTest {

    @Mock
    private ConversationUpdateReadByIdResource delegate;

    private ConversationUpdateReadByIdResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationUpdateReadByIdResourceImpl(delegate);
    }

    @Test
    void shouldDelegateUpdateReadById(@Random Long conversationId, @Random Long userId) {
        willDoNothing().given(delegate).updateReadById(conversationId, userId);

        sut.updateReadById(conversationId, userId);

        then(delegate).should().updateReadById(conversationId, userId);
        then(delegate).shouldHaveNoMoreInteractions();
    }
}
