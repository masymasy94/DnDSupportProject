package com.dndplatform.campaign.adapter.inbound.member;

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
class CampaignMemberListResourceImplTest {

    @Mock
    private CampaignMemberListDelegate delegate;

    private CampaignMemberListResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignMemberListResourceImpl(delegate);
    }

    @Test
    void shouldDelegateListMembers(@Random CampaignMemberViewModel member1,
                                   @Random CampaignMemberViewModel member2) {
        Long campaignId = 10L;
        List<CampaignMemberViewModel> expected = List.of(member1, member2);
        given(delegate.listMembers(campaignId)).willReturn(expected);

        List<CampaignMemberViewModel> result = sut.listMembers(campaignId);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).listMembers(campaignId);
        inOrder.verifyNoMoreInteractions();
    }
}
