package com.dndplatform.campaign.adapter.inbound.create;

import com.dndplatform.campaign.adapter.inbound.create.mapper.CampaignCreateMapper;
import com.dndplatform.campaign.adapter.inbound.create.mapper.CampaignViewModelMapper;
import com.dndplatform.campaign.domain.CampaignCreateService;
import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignCreate;
import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
import com.dndplatform.campaign.view.model.vm.CreateCampaignRequest;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CampaignCreateDelegateTest {

    @Mock
    private CampaignCreateService service;

    @Mock
    private CampaignCreateMapper createMapper;

    @Mock
    private CampaignViewModelMapper viewModelMapper;

    private CampaignCreateDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignCreateDelegate(service, createMapper, viewModelMapper);
    }

    @Test
    void shouldReturnCampaignViewModel(
            @Random CampaignCreate campaignCreate,
            @Random Campaign campaign,
            @Random CampaignViewModel campaignViewModel) {

        CreateCampaignRequest request = new CreateCampaignRequest(1L, "Test Campaign", "A description", 6, null);

        given(createMapper.apply(request)).willReturn(campaignCreate);
        given(service.create(any())).willReturn(campaign);
        given(viewModelMapper.apply(campaign)).willReturn(campaignViewModel);

        CampaignViewModel result = sut.create(request);

        assertThat(result).isEqualTo(campaignViewModel);

        var inOrder = inOrder(createMapper, service, viewModelMapper);
        then(createMapper).should(inOrder).apply(request);
        then(service).should(inOrder).create(any());
        then(viewModelMapper).should(inOrder).apply(campaign);
        inOrder.verifyNoMoreInteractions();
    }
}
