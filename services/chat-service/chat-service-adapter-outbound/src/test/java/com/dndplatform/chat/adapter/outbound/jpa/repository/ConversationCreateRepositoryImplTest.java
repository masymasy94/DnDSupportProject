package com.dndplatform.chat.adapter.outbound.jpa.repository;

import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationEntity;
import com.dndplatform.chat.adapter.outbound.jpa.mapper.ConversationMapper;
import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.domain.model.ConversationType;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ConversationCreateRepositoryImplTest {

    @Mock
    private ConversationPanacheRepository panacheRepository;

    @Mock
    private ConversationMapper mapper;

    private ConversationCreateRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationCreateRepositoryImpl(panacheRepository, mapper);
    }

    @Test
    void shouldPersistEntityAndReturnMappedConversation(@Random Conversation expected) {
        willDoNothing().given(panacheRepository).persist(any(ConversationEntity.class));
        willDoNothing().given(panacheRepository).flush();
        given(mapper.apply(any(ConversationEntity.class))).willReturn(expected);

        var input = new com.dndplatform.chat.domain.model.Conversation(
                null, "Test Channel", ConversationType.GROUP, 1L,
                LocalDateTime.now(), null, List.of());

        var result = sut.create(input);

        assertThat(result).isEqualTo(expected);
        then(panacheRepository).should().persist(any(ConversationEntity.class));
        then(panacheRepository).should().flush();
        then(mapper).should().apply(any(ConversationEntity.class));
    }
}
