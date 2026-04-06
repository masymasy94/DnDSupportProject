package com.dndplatform.campaign.adapter.inbound.note;

import com.dndplatform.campaign.adapter.inbound.note.mapper.CampaignNoteViewModelMapper;
import com.dndplatform.campaign.adapter.inbound.note.mapper.UpdateNoteMapper;
import com.dndplatform.campaign.domain.CampaignNoteUpdateService;
import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.model.CampaignNoteUpdate;
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
class CampaignNoteUpdateDelegateTest {

    @Mock
    private CampaignNoteUpdateService service;

    @Mock
    private CampaignNoteViewModelMapper viewModelMapper;

    @Mock
    private UpdateNoteMapper updateMapper;

    private CampaignNoteUpdateDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignNoteUpdateDelegate(service, viewModelMapper, updateMapper);
    }

    @Test
    void shouldReturnUpdatedNoteViewModel(
            @Random CampaignNoteUpdate campaignNoteUpdate,
            @Random CampaignNote campaignNote,
            @Random CampaignNoteViewModel noteViewModel) {

        Long campaignId = 1L;
        Long noteId = 10L;
        Long userId = 42L;
        UpdateNoteRequest request = new UpdateNoteRequest(userId, "Updated Title", "Updated content", "PUBLIC");

        given(updateMapper.apply(noteId, request)).willReturn(campaignNoteUpdate);
        given(service.update(campaignNoteUpdate, userId)).willReturn(campaignNote);
        given(viewModelMapper.apply(campaignNote)).willReturn(noteViewModel);

        CampaignNoteViewModel result = sut.update(campaignId, noteId, request);

        assertThat(result).isEqualTo(noteViewModel);

        var inOrder = inOrder(updateMapper, service, viewModelMapper);
        then(updateMapper).should(inOrder).apply(noteId, request);
        then(service).should(inOrder).update(campaignNoteUpdate, userId);
        then(viewModelMapper).should(inOrder).apply(campaignNote);
        inOrder.verifyNoMoreInteractions();
    }
}
