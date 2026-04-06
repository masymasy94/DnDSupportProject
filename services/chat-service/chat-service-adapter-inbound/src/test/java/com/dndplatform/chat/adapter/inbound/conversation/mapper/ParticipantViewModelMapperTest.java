package com.dndplatform.chat.adapter.inbound.conversation.mapper;

import com.dndplatform.chat.domain.model.ConversationParticipant;
import com.dndplatform.chat.domain.model.ConversationParticipantBuilder;
import com.dndplatform.chat.domain.model.ParticipantRole;
import com.dndplatform.chat.view.model.vm.ParticipantViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class ParticipantViewModelMapperTest {

    private ParticipantViewModelMapperImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ParticipantViewModelMapperImpl();
    }

    @Test
    void shouldMapParticipantToViewModel(@Random ConversationParticipant participant) {
        ParticipantViewModel result = sut.apply(participant);

        assertThat(result.userId()).isEqualTo(participant.userId());
        assertThat(result.role()).isEqualTo(participant.role().name());
        assertThat(result.joinedAt()).isEqualTo(participant.joinedAt());
        assertThat(result.lastReadAt()).isEqualTo(participant.lastReadAt());
    }

    @Test
    void shouldReturnNullRoleWhenRoleIsNull() {
        ConversationParticipant participant = ConversationParticipantBuilder.builder()
                .withId(1L)
                .withConversationId(10L)
                .withUserId(42L)
                .withRole(null)
                .withJoinedAt(LocalDateTime.now())
                .withLeftAt(null)
                .withLastReadAt(LocalDateTime.now())
                .build();

        ParticipantViewModel result = sut.apply(participant);

        assertThat(result.userId()).isEqualTo(42L);
        assertThat(result.role()).isNull();
    }

    @Test
    void shouldMapAdminRoleCorrectly() {
        ConversationParticipant participant = ConversationParticipantBuilder.builder()
                .withId(1L)
                .withConversationId(10L)
                .withUserId(1L)
                .withRole(ParticipantRole.ADMIN)
                .withJoinedAt(LocalDateTime.now())
                .withLeftAt(null)
                .withLastReadAt(null)
                .build();

        ParticipantViewModel result = sut.apply(participant);

        assertThat(result.role()).isEqualTo("ADMIN");
    }

    @Test
    void shouldMapMemberRoleCorrectly() {
        ConversationParticipant participant = ConversationParticipantBuilder.builder()
                .withId(2L)
                .withConversationId(10L)
                .withUserId(2L)
                .withRole(ParticipantRole.MEMBER)
                .withJoinedAt(LocalDateTime.now())
                .withLeftAt(null)
                .withLastReadAt(null)
                .build();

        ParticipantViewModel result = sut.apply(participant);

        assertThat(result.role()).isEqualTo("MEMBER");
    }
}
