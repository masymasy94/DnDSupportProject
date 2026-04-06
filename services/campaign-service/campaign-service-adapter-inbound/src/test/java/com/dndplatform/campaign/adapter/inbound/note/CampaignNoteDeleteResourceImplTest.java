package com.dndplatform.campaign.adapter.inbound.note;

import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CampaignNoteDeleteResourceImplTest {

    @Mock
    private CampaignNoteDeleteDelegate delegate;

    private CampaignNoteDeleteResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignNoteDeleteResourceImpl(delegate);
    }

    @Test
    void shouldDelegateDelete() {
        Long campaignId = 10L;
        Long noteId = 20L;
        Long userId = 1L;
        willDoNothing().given(delegate).delete(campaignId, noteId, userId);

        sut.delete(campaignId, noteId, userId);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).delete(campaignId, noteId, userId);
        inOrder.verifyNoMoreInteractions();
    }
}
