package com.dndplatform.campaign.adapter.inbound.note;

import com.dndplatform.campaign.view.model.vm.CampaignNoteViewModel;
import com.dndplatform.campaign.view.model.vm.CreateNoteRequest;
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
class CampaignNoteCreateResourceImplTest {

    @Mock
    private CampaignNoteCreateDelegate delegate;

    private CampaignNoteCreateResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignNoteCreateResourceImpl(delegate);
    }

    @Test
    void shouldDelegateCreate(@Random CreateNoteRequest request,
                              @Random CampaignNoteViewModel expected) {
        Long campaignId = 10L;
        given(delegate.create(campaignId, request)).willReturn(expected);

        CampaignNoteViewModel result = sut.create(campaignId, request);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).create(campaignId, request);
        inOrder.verifyNoMoreInteractions();
    }
}
