package com.dndplatform.campaign.adapter.inbound.update;

import com.dndplatform.campaign.adapter.inbound.create.mapper.CampaignViewModelMapper;
import com.dndplatform.campaign.adapter.inbound.update.mapper.CampaignUpdateMapper;
import com.dndplatform.campaign.domain.CampaignUpdateService;
import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignUpdate;
import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
import com.dndplatform.campaign.view.model.vm.UpdateCampaignRequest;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CampaignUpdateDelegateTest {

    @Mock
    private CampaignUpdateService service;

    @Mock
    private CampaignUpdateMapper updateMapper;

    @Mock
    private CampaignViewModelMapper viewModelMapper;

    private CampaignUpdateDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignUpdateDelegate(service, updateMapper, viewModelMapper);
    }

    @Test
    void shouldReturnUpdatedCampaignViewModel(
            @Random CampaignUpdate campaignUpdate,
            @Random Campaign campaign,
            @Random CampaignViewModel campaignViewModel) {

        Long campaignId = 1L;
        Long userId = 42L;
        UpdateCampaignRequest request = new UpdateCampaignRequest(userId, "Updated Name", "Updated desc", "ACTIVE", 6, null);

        given(updateMapper.apply(campaignId, request)).willReturn(campaignUpdate);
        given(service.update(campaignUpdate, userId)).willReturn(campaign);
        given(viewModelMapper.apply(campaign)).willReturn(campaignViewModel);

        CampaignViewModel result = sut.update(campaignId, request);

        assertThat(result).isEqualTo(campaignViewModel);

        var inOrder = inOrder(updateMapper, service, viewModelMapper);
        then(updateMapper).should(inOrder).apply(campaignId, request);
        then(service).should(inOrder).update(campaignUpdate, userId);
        then(viewModelMapper).should(inOrder).apply(campaign);
        inOrder.verifyNoMoreInteractions();
    }
}
