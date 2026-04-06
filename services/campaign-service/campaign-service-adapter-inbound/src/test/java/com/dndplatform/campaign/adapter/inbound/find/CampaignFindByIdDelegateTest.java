package com.dndplatform.campaign.adapter.inbound.find;

import com.dndplatform.campaign.adapter.inbound.create.mapper.CampaignViewModelMapper;
import com.dndplatform.campaign.domain.CampaignFindByIdService;
import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CampaignFindByIdDelegateTest {

    @Mock
    private CampaignFindByIdService service;

    @Mock
    private CampaignViewModelMapper viewModelMapper;

    private CampaignFindByIdDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignFindByIdDelegate(service, viewModelMapper);
    }

    @Test
    void shouldReturnCampaignViewModel(
            @Random Campaign campaign,
            @Random CampaignViewModel campaignViewModel) {

        Long campaignId = 1L;

        given(service.findById(campaignId)).willReturn(campaign);
        given(viewModelMapper.apply(campaign)).willReturn(campaignViewModel);

        CampaignViewModel result = sut.findById(campaignId);

        assertThat(result).isEqualTo(campaignViewModel);

        var inOrder = inOrder(service, viewModelMapper);
        then(service).should(inOrder).findById(campaignId);
        then(viewModelMapper).should(inOrder).apply(campaign);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenCampaignNotFound() {
        Long campaignId = 999L;

        willThrow(new NotFoundException("Campaign not found")).given(service).findById(campaignId);

        assertThatThrownBy(() -> sut.findById(campaignId))
                .isInstanceOf(NotFoundException.class);

        then(service).should().findById(campaignId);
        then(viewModelMapper).shouldHaveNoInteractions();
    }
}
