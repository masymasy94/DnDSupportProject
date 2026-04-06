package com.dndplatform.campaign.adapter.inbound.member;

import com.dndplatform.campaign.adapter.inbound.member.mapper.CampaignMemberViewModelMapper;
import com.dndplatform.campaign.domain.CampaignMemberAddService;
import com.dndplatform.campaign.domain.model.CampaignMember;
import com.dndplatform.campaign.view.model.vm.AddMemberRequest;
import com.dndplatform.campaign.view.model.vm.CampaignMemberViewModel;
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
class CampaignMemberAddDelegateTest {

    @Mock
    private CampaignMemberAddService service;

    @Mock
    private CampaignMemberViewModelMapper mapper;

    private CampaignMemberAddDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignMemberAddDelegate(service, mapper);
    }

    @Test
    void shouldReturnMemberViewModel(
            @Random CampaignMember campaignMember,
            @Random CampaignMemberViewModel memberViewModel) {

        Long campaignId = 1L;
        Long userId = 5L;
        Long characterId = 12L;
        Long requesterId = 1L;
        AddMemberRequest request = new AddMemberRequest(requesterId, userId, characterId);

        given(service.add(campaignId, userId, characterId, requesterId)).willReturn(campaignMember);
        given(mapper.apply(campaignMember)).willReturn(memberViewModel);

        CampaignMemberViewModel result = sut.addMember(campaignId, request);

        assertThat(result).isEqualTo(memberViewModel);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).add(campaignId, userId, characterId, requesterId);
        then(mapper).should(inOrder).apply(campaignMember);
        inOrder.verifyNoMoreInteractions();
    }
}
