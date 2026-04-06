package com.dndplatform.campaign.adapter.inbound.note;

import com.dndplatform.campaign.view.model.vm.CampaignNoteViewModel;
import com.dndplatform.campaign.view.model.vm.UpdateNoteRequest;
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
class CampaignNoteUpdateResourceImplTest {

    @Mock
    private CampaignNoteUpdateDelegate delegate;

    private CampaignNoteUpdateResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignNoteUpdateResourceImpl(delegate);
    }

    @Test
    void shouldDelegateUpdate(@Random UpdateNoteRequest request,
                              @Random CampaignNoteViewModel expected) {
        Long campaignId = 10L;
        Long noteId = 20L;
        given(delegate.update(campaignId, noteId, request)).willReturn(expected);

        CampaignNoteViewModel result = sut.update(campaignId, noteId, request);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).update(campaignId, noteId, request);
        inOrder.verifyNoMoreInteractions();
    }
}
