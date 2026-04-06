package com.dndplatform.chat.adapter.inbound.conversation.mapper;

import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.domain.model.ConversationBuilder;
import com.dndplatform.chat.domain.model.ConversationParticipant;
import com.dndplatform.chat.domain.model.ConversationParticipantBuilder;
import com.dndplatform.chat.domain.model.ConversationType;
import com.dndplatform.chat.domain.model.ParticipantRole;
import com.dndplatform.chat.view.model.vm.ConversationViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class ConversationViewModelMapperTest {

    private ConversationViewModelMapperImpl sut;

    @BeforeEach
    void setUp() throws Exception {
        sut = new ConversationViewModelMapperImpl();
        var field = ConversationViewModelMapperImpl.class.getDeclaredField("participantViewModelMapper");
        field.setAccessible(true);
        field.set(sut, new ParticipantViewModelMapperImpl());
    }

    @Test
    void shouldMapConversationToViewModel(@Random Conversation conversation) {
        ConversationViewModel result = sut.apply(conversation);

        assertThat(result.id()).isEqualTo(conversation.id());
        assertThat(result.name()).isEqualTo(conversation.name());
        assertThat(result.type()).isEqualTo(conversation.type().name());
        assertThat(result.createdBy()).isEqualTo(conversation.createdBy());
        assertThat(result.createdAt()).isEqualTo(conversation.createdAt());
        assertThat(result.updatedAt()).isEqualTo(conversation.updatedAt());
        assertThat(result.participants()).isNotNull();
        assertThat(result.participants()).hasSameSizeAs(conversation.participants());
    }

    @Test
    void shouldMapNullTypeToNullString() {
        Conversation conversation = ConversationBuilder.builder()
                .withId(1L)
                .withName("Test")
                .withType(null)
                .withCreatedBy(1L)
                .withCreatedAt(LocalDateTime.now())
                .withUpdatedAt(LocalDateTime.now())
                .withParticipants(List.of())
                .build();

        ConversationViewModel result = sut.apply(conversation);

        assertThat(result.type()).isNull();
    }

    @Test
    void shouldMapDirectTypeCorrectly() {
        Conversation conversation = ConversationBuilder.builder()
                .withId(1L)
                .withName(null)
                .withType(ConversationType.DIRECT)
                .withCreatedBy(1L)
                .withCreatedAt(LocalDateTime.now())
                .withUpdatedAt(LocalDateTime.now())
                .withParticipants(List.of())
                .build();

        ConversationViewModel result = sut.apply(conversation);

        assertThat(result.type()).isEqualTo("DIRECT");
    }

    @Test
    void shouldMapGroupTypeCorrectly() {
        Conversation conversation = ConversationBuilder.builder()
                .withId(2L)
                .withName("Party Chat")
                .withType(ConversationType.GROUP)
                .withCreatedBy(1L)
                .withCreatedAt(LocalDateTime.now())
                .withUpdatedAt(LocalDateTime.now())
                .withParticipants(List.of())
                .build();

        ConversationViewModel result = sut.apply(conversation);

        assertThat(result.type()).isEqualTo("GROUP");
    }

    @Test
    void shouldMapParticipantsCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        ConversationParticipant participant = ConversationParticipantBuilder.builder()
                .withId(1L)
                .withConversationId(1L)
                .withUserId(42L)
                .withRole(ParticipantRole.MEMBER)
                .withJoinedAt(now)
                .withLeftAt(null)
                .withLastReadAt(now)
                .build();

        Conversation conversation = ConversationBuilder.builder()
                .withId(1L)
                .withName("Group Chat")
                .withType(ConversationType.GROUP)
                .withCreatedBy(1L)
                .withCreatedAt(now)
                .withUpdatedAt(now)
                .withParticipants(List.of(participant))
                .build();

        ConversationViewModel result = sut.apply(conversation);

        assertThat(result.participants()).hasSize(1);
        assertThat(result.participants().get(0).userId()).isEqualTo(42L);
        assertThat(result.participants().get(0).role()).isEqualTo("MEMBER");
    }

    @Test
    void shouldReturnNullWhenConversationIsNull() {
        ConversationViewModel result = sut.apply(null);

        assertThat(result).isNull();
    }
}
