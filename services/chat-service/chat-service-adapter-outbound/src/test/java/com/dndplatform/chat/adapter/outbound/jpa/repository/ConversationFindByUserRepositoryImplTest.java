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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ConversationFindByUserRepositoryImplTest {

    @Mock
    private ConversationPanacheRepository panacheRepository;

    @Mock
    private ConversationMapper mapper;

    private ConversationFindByUserRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationFindByUserRepositoryImpl(panacheRepository, mapper);
    }

    @Test
    void shouldReturnMappedConversationsForUser(@Random Long userId,
                                                 @Random ConversationEntity entity,
                                                 @Random Conversation expected) {
        given(panacheRepository.findActiveByUserId(userId)).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        var result = sut.findByUserId(userId);

        assertThat(result).containsExactly(expected);
        then(panacheRepository).should().findActiveByUserId(userId);
        then(mapper).should().apply(entity);
    }

    @Test
    void shouldReturnEmptyListWhenNoConversationsFound(@Random Long userId) {
        given(panacheRepository.findActiveByUserId(userId)).willReturn(List.of());

        var result = sut.findByUserId(userId);

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
