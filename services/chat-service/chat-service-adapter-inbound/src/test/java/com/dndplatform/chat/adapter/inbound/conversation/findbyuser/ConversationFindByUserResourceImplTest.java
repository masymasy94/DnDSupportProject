package com.dndplatform.chat.adapter.inbound.conversation.findbyuser;

import com.dndplatform.chat.view.model.ConversationFindByUserResource;
import com.dndplatform.chat.view.model.vm.ConversationViewModel;
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

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ConversationFindByUserResourceImplTest {

    @Mock
    private ConversationFindByUserResource delegate;

    private ConversationFindByUserResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationFindByUserResourceImpl(delegate);
    }

    @Test
    void shouldDelegateFindByUser(@Random long userId, @Random ConversationViewModel vm1, @Random ConversationViewModel vm2) {
        var expected = List.of(vm1, vm2);
        given(delegate.findByUser(userId)).willReturn(expected);

        var result = sut.findByUser(userId);

        assertThat(result).isEqualTo(expected);
        then(delegate).should().findByUser(userId);
        then(delegate).shouldHaveNoMoreInteractions();
    }
}
