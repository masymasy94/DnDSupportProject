package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignQuestEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.model.QuestStatus;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CampaignQuestFindByCampaignRepositoryJpaTest {

    @Mock
    private CampaignQuestPanacheRepository panacheRepository;
    @Mock
    private CampaignEntityMapper mapper;

    private CampaignQuestFindByCampaignRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignQuestFindByCampaignRepositoryJpa(panacheRepository, mapper);
    }

    @Test
    void shouldReturnAllQuestsWhenStatusIsNull(@Random CampaignQuest expected) {
        Long campaignId = 1L;
        CampaignQuestEntity entity = new CampaignQuestEntity();
        given(panacheRepository.findByCampaignId(campaignId)).willReturn(List.of(entity));
        given(mapper.toCampaignQuest(entity)).willReturn(expected);

        List<CampaignQuest> result = sut.findByCampaign(campaignId, null);

        assertThat(result).containsExactly(expected);
        then(panacheRepository).should().findByCampaignId(campaignId);
        then(panacheRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void shouldFilterQuestsByStatusWhenStatusIsProvided(@Random CampaignQuest expected) {
        Long campaignId = 1L;
        QuestStatus status = QuestStatus.ACTIVE;
        CampaignQuestEntity entity = new CampaignQuestEntity();
        given(panacheRepository.findByCampaignIdAndStatus(campaignId, status.name())).willReturn(List.of(entity));
        given(mapper.toCampaignQuest(entity)).willReturn(expected);

        List<CampaignQuest> result = sut.findByCampaign(campaignId, status);

        assertThat(result).containsExactly(expected);
        then(panacheRepository).should().findByCampaignIdAndStatus(campaignId, status.name());
        then(panacheRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void shouldReturnEmptyListWhenNoQuestsFound() {
        Long campaignId = 2L;
        given(panacheRepository.findByCampaignId(campaignId)).willReturn(List.of());

        List<CampaignQuest> result = sut.findByCampaign(campaignId, null);

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
