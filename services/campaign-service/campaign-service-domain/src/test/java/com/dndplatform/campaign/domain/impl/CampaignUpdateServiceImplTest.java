package com.dndplatform.campaign.domain.impl;

import com.dndplatform.campaign.domain.CampaignUpdateService;
import com.dndplatform.campaign.domain.CampaignUpdateServiceImpl;
import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignStatus;
import com.dndplatform.campaign.domain.model.CampaignUpdate;
import com.dndplatform.campaign.domain.repository.CampaignFindByIdRepository;
import com.dndplatform.campaign.domain.repository.CampaignUpdateRepository;
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
class CampaignUpdateServiceImplTest {

    @Mock
    private CampaignFindByIdRepository findRepository;
    @Mock
    private CampaignUpdateRepository updateRepository;

    private CampaignUpdateService sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignUpdateServiceImpl(findRepository, updateRepository);
    }

    @Test
    void shouldUpdateCampaignWhenUserIsDungeonMaster(@Random CampaignUpdate input,
                                                     @Random Campaign existing,
                                                     @Random Campaign expected,
                                                     @Random Long userId) {
        existing = new Campaign(existing.id(), "Old Name", "Old Desc", userId, CampaignStatus.ACTIVE, 5, null, null, null);
        given(findRepository.findById(input.id())).willReturn(Optional.of(existing));
        given(updateRepository.update(input)).willReturn(expected);

        Campaign result = sut.update(input, userId);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(findRepository, updateRepository);
        then(findRepository).should(inOrder).findById(input.id());
        then(updateRepository).should(inOrder).update(input);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCampaignDoesNotExist(@Random CampaignUpdate input,
                                                               @Random Long userId) {
        given(findRepository.findById(input.id())).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.update(input, userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Campaign not found");

        then(updateRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowForbiddenExceptionWhenUserIsNotDungeonMaster(@Random CampaignUpdate input,
                                                                  @Random Campaign existing,
                                                                  @Random Long userId) {
        existing = new Campaign(input.id(), "Old Name", "Old Desc", 999L, CampaignStatus.ACTIVE, 5, null, null, null);
        given(findRepository.findById(input.id())).willReturn(Optional.of(existing));

        assertThatThrownBy(() -> sut.update(input, userId))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("Dungeon Master");

        then(updateRepository).shouldHaveNoInteractions();
    }
}
