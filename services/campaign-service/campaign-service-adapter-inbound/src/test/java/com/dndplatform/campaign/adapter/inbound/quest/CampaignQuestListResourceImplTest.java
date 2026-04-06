package com.dndplatform.campaign.adapter.inbound.quest;

import com.dndplatform.campaign.view.model.vm.CampaignQuestViewModel;
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
class CampaignQuestListResourceImplTest {

    @Mock
    private CampaignQuestListDelegate delegate;

    private CampaignQuestListResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignQuestListResourceImpl(delegate);
    }

    @Test
    void shouldDelegateList(@Random CampaignQuestViewModel quest1,
                            @Random CampaignQuestViewModel quest2) {
        Long campaignId = 10L;
        Long userId = 1L;
        String status = "ACTIVE";
        List<CampaignQuestViewModel> expected = List.of(quest1, quest2);
        given(delegate.list(campaignId, userId, status)).willReturn(expected);

        List<CampaignQuestViewModel> result = sut.list(campaignId, userId, status);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).list(campaignId, userId, status);
        inOrder.verifyNoMoreInteractions();
    }
}
