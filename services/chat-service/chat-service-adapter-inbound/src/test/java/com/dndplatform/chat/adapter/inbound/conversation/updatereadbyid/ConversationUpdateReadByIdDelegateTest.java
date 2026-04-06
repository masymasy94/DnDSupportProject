package com.dndplatform.chat.adapter.inbound.conversation.updatereadbyid;

import com.dndplatform.chat.domain.ConversationUpdateReadByIdService;
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
class ConversationUpdateReadByIdDelegateTest {

    @Mock
    private ConversationUpdateReadByIdService service;

    private ConversationUpdateReadByIdDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationUpdateReadByIdDelegate(service);
    }

    @Test
    void shouldDelegateToService(@Random Long conversationId, @Random Long userId) {
        willDoNothing().given(service).updateReadById(conversationId, userId);

        sut.updateReadById(conversationId, userId);

        then(service).should().updateReadById(conversationId, userId);
    }
}
