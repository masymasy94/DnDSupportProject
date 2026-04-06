package com.dndplatform.campaign.adapter.inbound.note;

import com.dndplatform.campaign.view.model.vm.CampaignNoteViewModel;
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
class CampaignNoteFindByIdResourceImplTest {

    @Mock
    private CampaignNoteFindByIdDelegate delegate;

    private CampaignNoteFindByIdResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignNoteFindByIdResourceImpl(delegate);
    }

    @Test
    void shouldDelegateFindById(@Random CampaignNoteViewModel expected) {
        Long campaignId = 10L;
        Long noteId = 20L;
        Long userId = 1L;
        given(delegate.findById(campaignId, noteId, userId)).willReturn(expected);

        CampaignNoteViewModel result = sut.findById(campaignId, noteId, userId);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findById(campaignId, noteId, userId);
        inOrder.verifyNoMoreInteractions();
    }
}
