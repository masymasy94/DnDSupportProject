package com.dndplatform.chat.adapter.outbound.jpa.repository;

import com.dndplatform.chat.adapter.outbound.jpa.entity.MessageEntity;
import com.dndplatform.chat.adapter.outbound.jpa.mapper.MessageMapper;
import com.dndplatform.chat.domain.model.Message;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class MessageFindByConversationRepositoryImplTest {

    @Mock
    private MessagePanacheRepository panacheRepository;

    @Mock
    private MessageMapper mapper;

    private MessageFindByConversationRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new MessageFindByConversationRepositoryImpl(panacheRepository, mapper);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturnPagedResultWithMappedMessages(@Random Long conversationId,
                                                    @Random MessageEntity entity,
                                                    @Random Message expected) {
        PanacheQuery<MessageEntity> mockQuery = mock(PanacheQuery.class);
        given(panacheRepository.queryByConversationId(conversationId)).willReturn(mockQuery);
        given(mockQuery.count()).willReturn(1L);
        given(mockQuery.page(anyInt(), anyInt())).willReturn(mockQuery);
        given(mockQuery.list()).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        var result = sut.findByConversationId(conversationId, 0, 10);

        assertThat(result.content()).containsExactly(expected);
        assertThat(result.totalElements()).isEqualTo(1L);
        then(panacheRepository).should().queryByConversationId(conversationId);
        then(mapper).should().apply(entity);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturnEmptyPagedResultWhenNoMessages(@Random Long conversationId) {
        PanacheQuery<MessageEntity> mockQuery = mock(PanacheQuery.class);
        given(panacheRepository.queryByConversationId(conversationId)).willReturn(mockQuery);
        given(mockQuery.count()).willReturn(0L);
        given(mockQuery.page(anyInt(), anyInt())).willReturn(mockQuery);
        given(mockQuery.list()).willReturn(List.of());

        var result = sut.findByConversationId(conversationId, 0, 10);

        assertThat(result.content()).isEmpty();
        assertThat(result.totalElements()).isZero();
        then(mapper).shouldHaveNoInteractions();
    }
}
