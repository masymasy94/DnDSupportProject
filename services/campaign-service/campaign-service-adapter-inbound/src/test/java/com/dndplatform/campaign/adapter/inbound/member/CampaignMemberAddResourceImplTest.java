package com.dndplatform.campaign.adapter.inbound.member;

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
class CampaignMemberAddResourceImplTest {

    @Mock
    private CampaignMemberAddDelegate delegate;

    private CampaignMemberAddResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignMemberAddResourceImpl(delegate);
    }

    @Test
    void shouldDelegateAddMember(@Random AddMemberRequest request,
                                 @Random CampaignMemberViewModel expected) {
        Long campaignId = 10L;
        given(delegate.addMember(campaignId, request)).willReturn(expected);

        CampaignMemberViewModel result = sut.addMember(campaignId, request);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).addMember(campaignId, request);
        inOrder.verifyNoMoreInteractions();
    }
}
