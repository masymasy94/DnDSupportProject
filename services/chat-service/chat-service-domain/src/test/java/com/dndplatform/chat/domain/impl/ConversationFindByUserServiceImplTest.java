package com.dndplatform.chat.domain.impl;

import com.dndplatform.chat.domain.ConversationFindByUserService;
import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.domain.repository.ConversationFindByUserRepository;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class ConversationFindByUserServiceImplTest {

    @Mock
    private ConversationFindByUserRepository repository;

    private ConversationFindByUserService sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationFindByUserServiceImpl(repository);
    }

    @Test
    void shouldFindConversationsByUserId(@Random Long userId, @Random Conversation conv1, @Random Conversation conv2) {
        List<Conversation> expected = List.of(conv1, conv2);

        given(repository.findByUserId(userId)).willReturn(expected);

        List<Conversation> result = sut.findByUserId(userId);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findByUserId(userId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoConversations(@Random Long userId) {
        given(repository.findByUserId(userId)).willReturn(List.of());

        List<Conversation> result = sut.findByUserId(userId);

        assertThat(result).isEmpty();
    }
}
