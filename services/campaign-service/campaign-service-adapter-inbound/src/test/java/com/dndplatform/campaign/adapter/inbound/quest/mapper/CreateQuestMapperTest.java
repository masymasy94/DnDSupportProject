package com.dndplatform.campaign.adapter.inbound.quest.mapper;

import com.dndplatform.campaign.domain.model.QuestPriority;
import com.dndplatform.campaign.domain.model.QuestStatus;
import com.dndplatform.campaign.view.model.vm.CreateQuestRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateQuestMapperTest {

    private CreateQuestMapper sut;

    @BeforeEach
    void setUp() {
        sut = new CreateQuestMapper();
    }

    @Test
    void shouldMapAllFields() {
        var request = new CreateQuestRequest(1L, "Find the Artifact", "Description", "COMPLETED", "SIDE");

        var result = sut.apply(10L, request, 42L);

        assertThat(result.campaignId()).isEqualTo(10L);
        assertThat(result.authorId()).isEqualTo(42L);
        assertThat(result.title()).isEqualTo("Find the Artifact");
        assertThat(result.description()).isEqualTo("Description");
        assertThat(result.status()).isEqualTo(QuestStatus.COMPLETED);
        assertThat(result.priority()).isEqualTo(QuestPriority.SIDE);
    }

    @Test
    void shouldDefaultStatusToActiveWhenNull() {
        var request = new CreateQuestRequest(1L, "Quest", "Desc", null, null);

        var result = sut.apply(10L, request, 42L);

        assertThat(result.status()).isEqualTo(QuestStatus.ACTIVE);
    }

    @Test
    void shouldDefaultPriorityToMainWhenNull() {
        var request = new CreateQuestRequest(1L, "Quest", "Desc", null, null);

        var result = sut.apply(10L, request, 42L);

        assertThat(result.priority()).isEqualTo(QuestPriority.MAIN);
    }
}
