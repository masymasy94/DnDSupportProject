package com.dndplatform.chat.adapter.outbound.jpa.repository;

import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationParticipantEntity;
import com.dndplatform.chat.adapter.outbound.jpa.mapper.ConversationParticipantMapper;
import com.dndplatform.chat.domain.model.ConversationParticipant;
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
class ParticipantFindByConversationRepositoryImplTest {

    @Mock
    private ParticipantPanacheRepository panacheRepository;

    @Mock
    private ConversationParticipantMapper mapper;

    private ParticipantFindByConversationRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ParticipantFindByConversationRepositoryImpl(panacheRepository, mapper);
    }

    @Test
    void shouldReturnMappedParticipantsForConversation(@Random Long conversationId,
                                                        @Random ConversationParticipantEntity entity,
                                                        @Random ConversationParticipant expected) {
        given(panacheRepository.findActiveByConversationId(conversationId)).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        var result = sut.findByConversationId(conversationId);

        assertThat(result).containsExactly(expected);
        then(panacheRepository).should().findActiveByConversationId(conversationId);
        then(mapper).should().apply(entity);
    }

    @Test
    void shouldReturnEmptyListWhenNoParticipants(@Random Long conversationId) {
        given(panacheRepository.findActiveByConversationId(conversationId)).willReturn(List.of());

        var result = sut.findByConversationId(conversationId);

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
