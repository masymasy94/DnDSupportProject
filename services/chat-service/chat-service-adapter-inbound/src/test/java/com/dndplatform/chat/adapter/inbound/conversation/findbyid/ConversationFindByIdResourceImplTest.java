package com.dndplatform.chat.adapter.inbound.conversation.findbyid;

import com.dndplatform.chat.view.model.ConversationFindByIdResource;
import com.dndplatform.chat.view.model.vm.ConversationViewModel;
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
class ConversationFindByIdResourceImplTest {

    @Mock
    private ConversationFindByIdResource delegate;

    private ConversationFindByIdResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationFindByIdResourceImpl(delegate);
    }

    @Test
    void shouldDelegateFindById(@Random Long id, @Random Long userId, @Random ConversationViewModel expected) {
        given(delegate.findById(id, userId)).willReturn(expected);

        var result = sut.findById(id, userId);

        assertThat(result).isEqualTo(expected);
        then(delegate).should().findById(id, userId);
        then(delegate).shouldHaveNoMoreInteractions();
    }
}
