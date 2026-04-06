package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignQuestEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.model.CampaignQuestCreate;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CampaignQuestCreateRepositoryJpaTest {

    @Mock
    private CampaignPanacheRepository campaignPanacheRepository;
    @Mock
    private CampaignQuestPanacheRepository questPanacheRepository;
    @Mock
    private CampaignEntityMapper mapper;

    private CampaignQuestCreateRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignQuestCreateRepositoryJpa(campaignPanacheRepository, questPanacheRepository, mapper);
    }

    @Test
    void shouldPersistQuestAndReturnMapped(@Random CampaignQuestCreate input, @Random CampaignQuest expected) {
        CampaignEntity campaign = new CampaignEntity();
        given(campaignPanacheRepository.findById(input.campaignId())).willReturn(campaign);
        willDoNothing().given(questPanacheRepository).persist(any(CampaignQuestEntity.class));
        given(mapper.toCampaignQuest(any(CampaignQuestEntity.class))).willReturn(expected);

        CampaignQuest result = sut.save(input);

        assertThat(result).isEqualTo(expected);
        then(campaignPanacheRepository).should().findById(input.campaignId());
        then(questPanacheRepository).should().persist(any(CampaignQuestEntity.class));
        then(mapper).should().toCampaignQuest(any(CampaignQuestEntity.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCampaignDoesNotExist(@Random CampaignQuestCreate input) {
        given(campaignPanacheRepository.findById(input.campaignId())).willReturn(null);

        assertThatThrownBy(() -> sut.save(input))
                .isInstanceOf(NotFoundException.class);

        then(questPanacheRepository).shouldHaveNoInteractions();
        then(mapper).shouldHaveNoInteractions();
    }
}
