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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ConversationFindByIdRepositoryImplTest {

    @Mock
    private ConversationPanacheRepository panacheRepository;

    @Mock
    private ConversationMapper mapper;

    private ConversationFindByIdRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationFindByIdRepositoryImpl(panacheRepository, mapper);
    }

    @Test
    void shouldReturnMappedConversationWhenFound(@Random Long id,
                                                  @Random ConversationEntity entity,
                                                  @Random Conversation expected) {
        given(panacheRepository.findByIdWithParticipants(id)).willReturn(Optional.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        var result = sut.findById(id);

        assertThat(result).isPresent().contains(expected);
        then(panacheRepository).should().findByIdWithParticipants(id);
        then(mapper).should().apply(entity);
    }

    @Test
    void shouldReturnEmptyWhenNotFound(@Random Long id) {
        given(panacheRepository.findByIdWithParticipants(id)).willReturn(Optional.empty());

        var result = sut.findById(id);

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
