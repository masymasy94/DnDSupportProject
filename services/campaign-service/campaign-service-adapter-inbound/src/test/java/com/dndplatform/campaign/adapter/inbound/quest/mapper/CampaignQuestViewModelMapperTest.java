package com.dndplatform.campaign.adapter.inbound.quest.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.campaign.domain.model.CampaignQuest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class CampaignQuestViewModelMapperTest {

    private CampaignQuestViewModelMapper sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignQuestViewModelMapper();
    }

    @Test
    void shouldMapAllFields(@Random CampaignQuest quest) {
        var result = sut.apply(quest);

        assertThat(result.id()).isEqualTo(quest.id());
        assertThat(result.campaignId()).isEqualTo(quest.campaignId());
        assertThat(result.authorId()).isEqualTo(quest.authorId());
        assertThat(result.title()).isEqualTo(quest.title());
        assertThat(result.description()).isEqualTo(quest.description());
        assertThat(result.status()).isEqualTo(quest.status().name());
        assertThat(result.priority()).isEqualTo(quest.priority().name());
        assertThat(result.createdAt()).isEqualTo(quest.createdAt());
        assertThat(result.updatedAt()).isEqualTo(quest.updatedAt());
    }
}
