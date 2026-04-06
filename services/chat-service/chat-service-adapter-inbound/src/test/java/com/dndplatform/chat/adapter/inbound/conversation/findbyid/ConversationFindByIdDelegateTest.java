package com.dndplatform.chat.adapter.inbound.conversation.findbyid;

import com.dndplatform.chat.adapter.inbound.conversation.mapper.ConversationViewModelMapper;
import com.dndplatform.chat.domain.ConversationFindByIdService;
import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.view.model.vm.ConversationViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ConversationFindByIdDelegateTest {

    @Mock
    private ConversationFindByIdService service;

    @Mock
    private ConversationViewModelMapper mapper;

    private ConversationFindByIdDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationFindByIdDelegate(service, mapper);
    }

    @Test
    void shouldReturnConversationViewModel(@Random Long id,
                                           @Random Long userId,
                                           @Random Conversation conversation,
                                           @Random ConversationViewModel expected) {
        given(service.findById(id, userId)).willReturn(Optional.of(conversation));
        given(mapper.apply(conversation)).willReturn(expected);

        var actual = sut.findById(id, userId);

        assertThat(actual).isEqualTo(expected);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).findById(id, userId);
        then(mapper).should(inOrder).apply(conversation);
    }

    @Test
    void shouldThrowNotFoundWhenConversationMissing(@Random Long id, @Random Long userId) {
        given(service.findById(id, userId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.findById(id, userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Conversation not found");
    }
}
