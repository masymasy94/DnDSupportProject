package com.dndplatform.campaign.adapter.inbound.note;

import com.dndplatform.campaign.view.model.vm.CampaignNoteViewModel;
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
class CampaignNoteListResourceImplTest {

    @Mock
    private CampaignNoteListDelegate delegate;

    private CampaignNoteListResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignNoteListResourceImpl(delegate);
    }

    @Test
    void shouldDelegateListNotes(@Random CampaignNoteViewModel note1,
                                 @Random CampaignNoteViewModel note2) {
        Long campaignId = 10L;
        Long userId = 1L;
        List<CampaignNoteViewModel> expected = List.of(note1, note2);
        given(delegate.listNotes(campaignId, userId)).willReturn(expected);

        List<CampaignNoteViewModel> result = sut.listNotes(campaignId, userId);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).listNotes(campaignId, userId);
        inOrder.verifyNoMoreInteractions();
    }
}
