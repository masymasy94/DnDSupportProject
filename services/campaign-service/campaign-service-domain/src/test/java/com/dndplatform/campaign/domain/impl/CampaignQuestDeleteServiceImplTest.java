package com.dndplatform.campaign.domain.impl;

import com.dndplatform.campaign.domain.CampaignQuestDeleteService;
import com.dndplatform.campaign.domain.CampaignQuestDeleteServiceImpl;
import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.model.CampaignStatus;
import com.dndplatform.campaign.domain.model.QuestStatus;
import com.dndplatform.campaign.domain.repository.CampaignFindByIdRepository;
import com.dndplatform.campaign.domain.repository.CampaignQuestDeleteRepository;
import com.dndplatform.campaign.domain.repository.CampaignQuestFindByIdRepository;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class CampaignQuestDeleteServiceImplTest {

    @Mock
    private CampaignFindByIdRepository campaignFindRepository;
    @Mock
    private CampaignQuestFindByIdRepository questFindByIdRepository;
    @Mock
    private CampaignQuestDeleteRepository questDeleteRepository;

    private CampaignQuestDeleteService sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignQuestDeleteServiceImpl(campaignFindRepository, questFindByIdRepository, questDeleteRepository);
    }

    @Test
    void shouldDeleteQuestWhenUserIsDungeonMaster(@Random CampaignQuest quest,
                                                    @Random Campaign campaign,
                                                    @Random Long campaignId,
                                                    @Random Long questId,
                                                    @Random Long userId) {
        campaign = new Campaign(campaignId, "Test", "Desc", userId, CampaignStatus.ACTIVE, 5, null, null, null);
        given(questFindByIdRepository.findById(questId)).willReturn(Optional.of(quest));
        given(campaignFindRepository.findById(campaignId)).willReturn(Optional.of(campaign));

        sut.delete(campaignId, questId, userId);

        var inOrder = inOrder(questFindByIdRepository, campaignFindRepository, questDeleteRepository);
        then(questFindByIdRepository).should(inOrder).findById(questId);
        then(campaignFindRepository).should(inOrder).findById(campaignId);
        then(questDeleteRepository).should(inOrder).deleteById(questId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldDeleteQuestWhenUserIsAuthor(@Random CampaignQuest quest,
                                             @Random Campaign campaign,
                                             @Random Long campaignId,
                                             @Random Long questId,
                                             @Random Long userId) {
        quest = new CampaignQuest(questId, campaignId, userId, "Title", "Desc", QuestStatus.ACTIVE, null, null, null);
        campaign = new Campaign(campaignId, "Test", "Desc", 999L, CampaignStatus.ACTIVE, 5, null, null, null);
        given(questFindByIdRepository.findById(questId)).willReturn(Optional.of(quest));
        given(campaignFindRepository.findById(campaignId)).willReturn(Optional.of(campaign));

        sut.delete(campaignId, questId, userId);

        var inOrder = inOrder(questFindByIdRepository, campaignFindRepository, questDeleteRepository);
        then(questFindByIdRepository).should(inOrder).findById(questId);
        then(campaignFindRepository).should(inOrder).findById(campaignId);
        then(questDeleteRepository).should(inOrder).deleteById(questId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenQuestDoesNotExist(@Random Long campaignId,
                                                            @Random Long questId,
                                                            @Random Long userId) {
        given(questFindByIdRepository.findById(questId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.delete(campaignId, questId, userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Quest not found");

        then(campaignFindRepository).shouldHaveNoInteractions();
        then(questDeleteRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowForbiddenExceptionWhenUserIsNotAuthorOrDm(@Random CampaignQuest quest,
                                                               @Random Campaign campaign,
                                                               @Random Long campaignId,
                                                               @Random Long questId,
                                                               @Random Long userId) {
        quest = new CampaignQuest(questId, campaignId, 999L, "Title", "Desc", QuestStatus.ACTIVE, null, null, null);
        campaign = new Campaign(campaignId, "Test", "Desc", 888L, CampaignStatus.ACTIVE, 5, null, null, null);
        given(questFindByIdRepository.findById(questId)).willReturn(Optional.of(quest));
        given(campaignFindRepository.findById(campaignId)).willReturn(Optional.of(campaign));

        assertThatThrownBy(() -> sut.delete(campaignId, questId, userId))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("author or Dungeon Master");

        then(questDeleteRepository).shouldHaveNoInteractions();
    }
}
