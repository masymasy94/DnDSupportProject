package com.dndplatform.chat.adapter.outbound.jpa.mapper;

import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationEntity;
import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationParticipantEntity;
import com.dndplatform.chat.domain.model.ConversationParticipant;
import com.dndplatform.chat.domain.model.ParticipantRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ConversationParticipantMapperTest {

    private final ConversationParticipantMapper sut = new ConversationParticipantMapperImpl();

    @Test
    void shouldMapEntityToDomain() {
        ConversationEntity conversationEntity = new ConversationEntity();
        conversationEntity.id = 10L;

        ConversationParticipantEntity entity = new ConversationParticipantEntity();
        entity.id = 1L;
        entity.conversation = conversationEntity;
        entity.userId = 42L;
        entity.role = "ADMIN";
        entity.joinedAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        entity.leftAt = LocalDateTime.of(2024, 6, 1, 10, 0);
        entity.lastReadAt = LocalDateTime.of(2024, 1, 15, 12, 0);

        ConversationParticipant result = sut.apply(entity);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.conversationId()).isEqualTo(10L);
        assertThat(result.userId()).isEqualTo(42L);
        assertThat(result.role()).isEqualTo(ParticipantRole.ADMIN);
        assertThat(result.joinedAt()).isEqualTo(entity.joinedAt);
        assertThat(result.leftAt()).isEqualTo(entity.leftAt);
        assertThat(result.lastReadAt()).isEqualTo(entity.lastReadAt);
    }

    @Test
    void shouldMapMemberRoleWhenRoleIsMember() {
        ConversationParticipantEntity entity = new ConversationParticipantEntity();
        entity.id = 2L;
        entity.conversation = null;
        entity.userId = 99L;
        entity.role = "MEMBER";

        ConversationParticipant result = sut.apply(entity);

        assertThat(result.role()).isEqualTo(ParticipantRole.MEMBER);
        assertThat(result.conversationId()).isNull();
    }

    @Test
    void shouldDefaultToMemberRoleWhenRoleIsNull() {
        ConversationParticipantEntity entity = new ConversationParticipantEntity();
        entity.id = 3L;
        entity.conversation = null;
        entity.userId = 7L;
        entity.role = null;

        ConversationParticipant result = sut.apply(entity);

        assertThat(result.role()).isEqualTo(ParticipantRole.MEMBER);
    }

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        ConversationParticipant result = sut.apply(null);

        assertThat(result).isNull();
    }

    @Test
    void shouldMapNullConversationToNullConversationId() {
        ConversationParticipantEntity entity = new ConversationParticipantEntity();
        entity.id = 5L;
        entity.conversation = null;
        entity.userId = 11L;
        entity.role = "ADMIN";

        ConversationParticipant result = sut.apply(entity);

        assertThat(result.conversationId()).isNull();
    }

    @Test
    void shouldDefaultRoleWhenMapRoleCalledWithNull() {
        ParticipantRole role = sut.mapRole(null);

        assertThat(role).isEqualTo(ParticipantRole.MEMBER);
    }

    @Test
    void shouldMapRoleWhenMapRoleCalledWithValue() {
        ParticipantRole role = sut.mapRole("ADMIN");

        assertThat(role).isEqualTo(ParticipantRole.ADMIN);
    }
}
