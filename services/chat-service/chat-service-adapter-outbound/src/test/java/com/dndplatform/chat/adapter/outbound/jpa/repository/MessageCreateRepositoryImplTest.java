package com.dndplatform.chat.adapter.outbound.jpa.repository;

import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationEntity;
import com.dndplatform.chat.adapter.outbound.jpa.entity.MessageEntity;
import com.dndplatform.chat.adapter.outbound.jpa.mapper.MessageMapper;
import com.dndplatform.chat.domain.model.Message;
import com.dndplatform.chat.domain.model.MessageType;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class MessageCreateRepositoryImplTest {

    @Mock
    private MessagePanacheRepository panacheRepository;

    @Mock
    private ConversationPanacheRepository conversationPanacheRepository;

    @Mock
    private MessageMapper mapper;

    private MessageCreateRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new MessageCreateRepositoryImpl(panacheRepository, conversationPanacheRepository, mapper);
    }

    @Test
    void shouldPersistMessageAndReturnMapped(@Random Message expected) {
        var conversationRef = new ConversationEntity();

        given(conversationPanacheRepository.findById(expected.conversationId()))
                .willReturn(conversationRef);
        willDoNothing().given(panacheRepository).persist(any(MessageEntity.class));
        given(conversationPanacheRepository.update(any(String.class), any(LocalDateTime.class), anyLong()))
                .willReturn(1);
        given(mapper.apply(any(MessageEntity.class))).willReturn(expected);

        var input = new Message(
                null, expected.conversationId(), 1L, "Hello", MessageType.TEXT, LocalDateTime.now(), null, null);

        var result = sut.create(input);

        assertThat(result).isEqualTo(expected);
        then(panacheRepository).should().persist(any(MessageEntity.class));
        then(conversationPanacheRepository).should().update(
                eq("updatedAt = ?1 where id = ?2"), any(LocalDateTime.class), eq(input.conversationId()));
        then(mapper).should().apply(any(MessageEntity.class));
    }
}
