package com.dndplatform.campaign.domain.impl;

import com.dndplatform.campaign.domain.CampaignQuestFindByCampaignService;
import com.dndplatform.campaign.domain.CampaignQuestFindByCampaignServiceImpl;
import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.model.QuestStatus;
import com.dndplatform.campaign.domain.repository.CampaignQuestFindByCampaignRepository;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class CampaignQuestFindByCampaignServiceImplTest {

    @Mock
    private CampaignQuestFindByCampaignRepository repository;

    private CampaignQuestFindByCampaignService sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignQuestFindByCampaignServiceImpl(repository);
    }

    @Test
    void shouldReturnQuestsForCampaign(@Random CampaignQuest quest1,
                                         @Random CampaignQuest quest2,
                                         @Random Long campaignId) {
        List<CampaignQuest> expected = List.of(quest1, quest2);
        given(repository.findByCampaign(campaignId, null)).willReturn(expected);

        List<CampaignQuest> result = sut.findByCampaign(campaignId, null);

        assertThat(result).isEqualTo(expected);
        then(repository).should().findByCampaign(campaignId, null);
    }

    @Test
    void shouldReturnQuestsForCampaignWithStatus(@Random CampaignQuest quest1,
                                                  @Random CampaignQuest quest2,
                                                  @Random Long campaignId) {
        List<CampaignQuest> expected = List.of(quest1, quest2);
        QuestStatus status = QuestStatus.ACTIVE;
        given(repository.findByCampaign(campaignId, status)).willReturn(expected);

        List<CampaignQuest> result = sut.findByCampaign(campaignId, status);

        assertThat(result).isEqualTo(expected);
        then(repository).should().findByCampaign(campaignId, status);
    }
}
