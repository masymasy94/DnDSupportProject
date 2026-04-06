package com.dndplatform.campaign.adapter.inbound.note;

import com.dndplatform.campaign.adapter.inbound.note.mapper.CampaignNoteViewModelMapper;
import com.dndplatform.campaign.domain.CampaignNoteFindVisibleService;
import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.model.NoteVisibility;
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
class CampaignNoteListDelegateTest {

    @Mock
    private CampaignNoteFindVisibleService service;

    @Mock
    private CampaignNoteViewModelMapper viewModelMapper;

    private CampaignNoteListDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignNoteListDelegate(service, viewModelMapper);
    }

    @Test
    void shouldReturnVisibleNoteViewModels(
            @Random CampaignNoteViewModel vm1,
            @Random CampaignNoteViewModel vm2) {

        Long campaignId = 1L;
        Long userId = 42L;

        CampaignNote note1 = new CampaignNote(1L, campaignId, userId, "Note One", "Content one", NoteVisibility.PUBLIC, null, null);
        CampaignNote note2 = new CampaignNote(2L, campaignId, userId, "Note Two", "Content two", NoteVisibility.PUBLIC, null, null);

        given(service.findVisibleNotes(campaignId, userId)).willReturn(List.of(note1, note2));
        given(viewModelMapper.apply(note1)).willReturn(vm1);
        given(viewModelMapper.apply(note2)).willReturn(vm2);

        List<CampaignNoteViewModel> result = sut.listNotes(campaignId, userId);

        assertThat(result).containsExactly(vm1, vm2);

        var inOrder = inOrder(service, viewModelMapper);
        then(service).should(inOrder).findVisibleNotes(campaignId, userId);
        then(viewModelMapper).should(inOrder).apply(note1);
        then(viewModelMapper).should(inOrder).apply(note2);
        inOrder.verifyNoMoreInteractions();
    }
}
