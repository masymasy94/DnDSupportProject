package com.dndplatform.chat.adapter.inbound.conversation.create;

import com.dndplatform.chat.view.model.ConversationCreateResource;
import com.dndplatform.chat.view.model.vm.ConversationViewModel;
import com.dndplatform.chat.view.model.vm.CreateConversationViewModel;
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
class ConversationCreateResourceImplTest {

    @Mock
    private ConversationCreateResource delegate;

    private ConversationCreateResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationCreateResourceImpl(delegate);
    }

    @Test
    void shouldDelegateCreate(@Random Long userId, @Random CreateConversationViewModel request, @Random ConversationViewModel expected) {
        given(delegate.create(userId, request)).willReturn(expected);

        var result = sut.create(userId, request);

        assertThat(result).isEqualTo(expected);
        then(delegate).should().create(userId, request);
        then(delegate).shouldHaveNoMoreInteractions();
    }
}
