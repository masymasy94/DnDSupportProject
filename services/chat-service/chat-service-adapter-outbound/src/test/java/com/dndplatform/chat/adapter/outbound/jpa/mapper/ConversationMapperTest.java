package com.dndplatform.chat.adapter.outbound.jpa.mapper;

import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationEntity;
import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationParticipantEntity;
import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.domain.model.ConversationType;
import com.dndplatform.chat.domain.model.ParticipantRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ConversationMapperTest {

    private ConversationMapper sut;

    @BeforeEach
    void setUp() throws Exception {
        ConversationParticipantMapperImpl participantMapper = new ConversationParticipantMapperImpl();
        ConversationMapperImpl impl = new ConversationMapperImpl();
        Field field = ConversationMapperImpl.class.getDeclaredField("conversationParticipantMapper");
        field.setAccessible(true);
        field.set(impl, participantMapper);
        sut = impl;
    }

    @Test
    void shouldMapEntityToDomain() {
        ConversationParticipantEntity participantEntity = new ConversationParticipantEntity();
        participantEntity.id = 11L;
        participantEntity.userId = 5L;
        participantEntity.role = "ADMIN";
        participantEntity.joinedAt = LocalDateTime.of(2024, 2, 1, 9, 0);

        ConversationEntity entity = new ConversationEntity();
        entity.id = 7L;
        entity.name = "My Group";
        entity.type = "GROUP";
        entity.createdBy = 1L;
        entity.createdAt = LocalDateTime.of(2024, 1, 1, 8, 0);
        entity.updatedAt = LocalDateTime.of(2024, 3, 1, 8, 0);
        entity.participants = List.of(participantEntity);

        // Link participant back to conversation for conversationId extraction
        participantEntity.conversation = entity;

        Conversation result = sut.apply(entity);

        assertThat(result.id()).isEqualTo(7L);
        assertThat(result.name()).isEqualTo("My Group");
        assertThat(result.type()).isEqualTo(ConversationType.GROUP);
        assertThat(result.createdBy()).isEqualTo(1L);
        assertThat(result.createdAt()).isEqualTo(entity.createdAt);
        assertThat(result.updatedAt()).isEqualTo(entity.updatedAt);
        assertThat(result.participants()).hasSize(1);
        assertThat(result.participants().get(0).id()).isEqualTo(11L);
        assertThat(result.participants().get(0).role()).isEqualTo(ParticipantRole.ADMIN);
    }

    @Test
    void shouldMapDirectTypeConversation() {
        ConversationEntity entity = new ConversationEntity();
        entity.id = 2L;
        entity.name = null;
        entity.type = "DIRECT";
        entity.createdBy = 2L;
        entity.participants = List.of();

        Conversation result = sut.apply(entity);

        assertThat(result.type()).isEqualTo(ConversationType.DIRECT);
        assertThat(result.name()).isNull();
        assertThat(result.participants()).isEmpty();
    }

    @Test
    void shouldDefaultToDirectTypeWhenTypeIsNull() {
        ConversationEntity entity = new ConversationEntity();
        entity.id = 3L;
        entity.type = null;
        entity.createdBy = 3L;
        entity.participants = List.of();

        Conversation result = sut.apply(entity);

        assertThat(result.type()).isEqualTo(ConversationType.DIRECT);
    }

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        Conversation result = sut.apply(null);

        assertThat(result).isNull();
    }

    @Test
    void shouldMapNullParticipantsListToNull() {
        ConversationEntity entity = new ConversationEntity();
        entity.id = 4L;
        entity.type = "GROUP";
        entity.createdBy = 1L;
        entity.participants = null;

        Conversation result = sut.apply(entity);

        assertThat(result.participants()).isNull();
    }

    @Test
    void shouldDefaultToDirectTypeWhenMapTypeCalledWithNull() {
        ConversationType type = sut.mapType(null);

        assertThat(type).isEqualTo(ConversationType.DIRECT);
    }

    @Test
    void shouldMapGroupTypeWhenMapTypeCalledWithValue() {
        ConversationType type = sut.mapType("GROUP");

        assertThat(type).isEqualTo(ConversationType.GROUP);
    }
}
