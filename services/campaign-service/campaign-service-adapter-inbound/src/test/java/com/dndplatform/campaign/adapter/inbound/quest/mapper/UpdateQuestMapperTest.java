package com.dndplatform.campaign.adapter.inbound.quest.mapper;

import com.dndplatform.campaign.domain.model.QuestPriority;
import com.dndplatform.campaign.domain.model.QuestStatus;
import com.dndplatform.campaign.view.model.vm.UpdateQuestRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateQuestMapperTest {

    private UpdateQuestMapper sut;

    @BeforeEach
    void setUp() {
        sut = new UpdateQuestMapper();
    }

    @Test
    void shouldMapAllFields() {
        var request = new UpdateQuestRequest(1L, "Updated Quest", "Updated desc", "FAILED", "SIDE");

        var result = sut.apply(7L, request);

        assertThat(result.id()).isEqualTo(7L);
        assertThat(result.title()).isEqualTo("Updated Quest");
        assertThat(result.description()).isEqualTo("Updated desc");
        assertThat(result.status()).isEqualTo(QuestStatus.FAILED);
        assertThat(result.priority()).isEqualTo(QuestPriority.SIDE);
    }

    @Test
    void shouldMapNullStatusToNull() {
        var request = new UpdateQuestRequest(1L, "Quest", "Desc", null, "MAIN");

        var result = sut.apply(7L, request);

        assertThat(result.status()).isNull();
    }

    @Test
    void shouldMapNullPriorityToNull() {
        var request = new UpdateQuestRequest(1L, "Quest", "Desc", "ACTIVE", null);

        var result = sut.apply(7L, request);

        assertThat(result.priority()).isNull();
    }
}
