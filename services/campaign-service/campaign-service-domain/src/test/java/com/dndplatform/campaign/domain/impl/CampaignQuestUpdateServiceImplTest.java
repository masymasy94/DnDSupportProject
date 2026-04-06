package com.dndplatform.campaign.domain.impl;

import com.dndplatform.campaign.domain.CampaignQuestUpdateService;
import com.dndplatform.campaign.domain.CampaignQuestUpdateServiceImpl;
import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.model.CampaignQuestUpdate;
import com.dndplatform.campaign.domain.model.CampaignStatus;
import com.dndplatform.campaign.domain.model.QuestStatus;
import com.dndplatform.campaign.domain.repository.CampaignFindByIdRepository;
import com.dndplatform.campaign.domain.repository.CampaignQuestFindByIdRepository;
import com.dndplatform.campaign.domain.repository.CampaignQuestUpdateRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class CampaignQuestUpdateServiceImplTest {

    @Mock
    private CampaignFindByIdRepository campaignFindRepository;
    @Mock
    private CampaignQuestFindByIdRepository questFindByIdRepository;
    @Mock
    private CampaignQuestUpdateRepository questUpdateRepository;

    private CampaignQuestUpdateService sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignQuestUpdateServiceImpl(campaignFindRepository, questFindByIdRepository, questUpdateRepository);
    }

    @Test
    void shouldUpdateQuestWhenUserIsDungeonMaster(@Random CampaignQuestUpdate input,
                                                   @Random CampaignQuest existing,
                                                   @Random CampaignQuest expected,
                                                   @Random Long campaignId,
                                                   @Random Long userId) {
        existing = new CampaignQuest(input.id(), campaignId, 999L, "Old Title", "Desc", QuestStatus.ACTIVE, null, null, null);
        Campaign campaign = new Campaign(campaignId, "Test", "Desc", userId, CampaignStatus.ACTIVE, 5, null, null, null);
        given(questFindByIdRepository.findById(input.id())).willReturn(Optional.of(existing));
        given(campaignFindRepository.findById(campaignId)).willReturn(Optional.of(campaign));
        given(questUpdateRepository.update(input)).willReturn(expected);

        CampaignQuest result = sut.update(input, campaignId, userId);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(questFindByIdRepository, campaignFindRepository, questUpdateRepository);
        then(questFindByIdRepository).should(inOrder).findById(input.id());
        then(campaignFindRepository).should(inOrder).findById(campaignId);
        then(questUpdateRepository).should(inOrder).update(input);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldUpdateQuestWhenUserIsAuthor(@Random CampaignQuestUpdate input,
                                             @Random CampaignQuest existing,
                                             @Random CampaignQuest expected,
                                             @Random Long campaignId,
                                             @Random Long userId) {
        existing = new CampaignQuest(input.id(), campaignId, userId, "Old Title", "Desc", QuestStatus.ACTIVE, null, null, null);
        Campaign campaign = new Campaign(campaignId, "Test", "Desc", 999L, CampaignStatus.ACTIVE, 5, null, null, null);
        given(questFindByIdRepository.findById(input.id())).willReturn(Optional.of(existing));
        given(campaignFindRepository.findById(campaignId)).willReturn(Optional.of(campaign));
        given(questUpdateRepository.update(input)).willReturn(expected);

        CampaignQuest result = sut.update(input, campaignId, userId);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenQuestDoesNotExist(@Random CampaignQuestUpdate input,
                                                            @Random Long campaignId,
                                                            @Random Long userId) {
        given(questFindByIdRepository.findById(input.id())).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.update(input, campaignId, userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Quest not found");

        then(campaignFindRepository).shouldHaveNoInteractions();
        then(questUpdateRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowForbiddenExceptionWhenUserIsNotAuthorOrDm(@Random CampaignQuestUpdate input,
                                                                @Random CampaignQuest existing,
                                                                @Random Long campaignId,
                                                                @Random Long userId) {
        existing = new CampaignQuest(input.id(), campaignId, 999L, "Old Title", "Desc", QuestStatus.ACTIVE, null, null, null);
        Campaign campaign = new Campaign(campaignId, "Test", "Desc", 888L, CampaignStatus.ACTIVE, 5, null, null, null);
        given(questFindByIdRepository.findById(input.id())).willReturn(Optional.of(existing));
        given(campaignFindRepository.findById(campaignId)).willReturn(Optional.of(campaign));

        assertThatThrownBy(() -> sut.update(input, campaignId, userId))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("author or Dungeon Master");

        then(questUpdateRepository).shouldHaveNoInteractions();
    }
}
