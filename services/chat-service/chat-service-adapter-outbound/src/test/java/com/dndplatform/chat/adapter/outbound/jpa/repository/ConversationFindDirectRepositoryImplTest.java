package com.dndplatform.chat.adapter.outbound.jpa.repository;

import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationEntity;
import com.dndplatform.chat.adapter.outbound.jpa.mapper.ConversationMapper;
import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ConversationFindDirectRepositoryImplTest {

    @Mock
    private ConversationPanacheRepository panacheRepository;

    @Mock
    private ConversationMapper mapper;

    private ConversationFindDirectRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationFindDirectRepositoryImpl(panacheRepository, mapper);
    }

    @Test
    void shouldReturnMappedConversationWhenDirectConversationExists(@Random Long userId1,
                                                                     @Random Long userId2,
                                                                     @Random ConversationEntity entity,
                                                                     @Random Conversation expected) {
        given(panacheRepository.findDirectConversation(userId1, userId2)).willReturn(Optional.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        var result = sut.findDirectConversation(userId1, userId2);

        assertThat(result).isPresent().contains(expected);
        then(panacheRepository).should().findDirectConversation(userId1, userId2);
        then(mapper).should().apply(entity);
    }

    @Test
    void shouldReturnEmptyWhenNoDirectConversationExists(@Random Long userId1, @Random Long userId2) {
        given(panacheRepository.findDirectConversation(userId1, userId2)).willReturn(Optional.empty());

        var result = sut.findDirectConversation(userId1, userId2);

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
