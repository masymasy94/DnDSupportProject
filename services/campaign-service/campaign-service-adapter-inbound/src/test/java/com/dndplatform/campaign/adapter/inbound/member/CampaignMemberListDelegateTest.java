package com.dndplatform.campaign.adapter.inbound.member;

import com.dndplatform.campaign.adapter.inbound.member.mapper.CampaignMemberViewModelMapper;
import com.dndplatform.campaign.domain.CampaignMemberFindService;
import com.dndplatform.campaign.domain.model.CampaignMember;
import com.dndplatform.campaign.domain.model.MemberRole;
import com.dndplatform.campaign.view.model.vm.CampaignMemberViewModel;
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
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CampaignMemberListDelegateTest {

    @Mock
    private CampaignMemberFindService service;

    @Mock
    private CampaignMemberViewModelMapper mapper;

    private CampaignMemberListDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignMemberListDelegate(service, mapper);
    }

    @Test
    void shouldReturnMemberViewModels(
            @Random CampaignMemberViewModel vm1,
            @Random CampaignMemberViewModel vm2) {

        Long campaignId = 1L;

        CampaignMember member1 = new CampaignMember(1L, campaignId, 5L, null, MemberRole.PLAYER, null);
        CampaignMember member2 = new CampaignMember(2L, campaignId, 6L, null, MemberRole.PLAYER, null);

        given(service.findByCampaignId(campaignId)).willReturn(List.of(member1, member2));
        given(mapper.apply(member1)).willReturn(vm1);
        given(mapper.apply(member2)).willReturn(vm2);

        List<CampaignMemberViewModel> result = sut.listMembers(campaignId);

        assertThat(result).containsExactly(vm1, vm2);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).findByCampaignId(campaignId);
        then(mapper).should(inOrder).apply(member1);
        then(mapper).should(inOrder).apply(member2);
        inOrder.verifyNoMoreInteractions();
    }
}
