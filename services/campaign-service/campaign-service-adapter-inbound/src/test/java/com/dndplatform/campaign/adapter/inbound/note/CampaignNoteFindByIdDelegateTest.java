package com.dndplatform.campaign.adapter.inbound.note;

import com.dndplatform.campaign.adapter.inbound.note.mapper.CampaignNoteViewModelMapper;
import com.dndplatform.campaign.domain.CampaignNoteFindByIdService;
import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.view.model.vm.CampaignNoteViewModel;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CampaignNoteFindByIdDelegateTest {

    @Mock
    private CampaignNoteFindByIdService service;

    @Mock
    private CampaignNoteViewModelMapper viewModelMapper;

    private CampaignNoteFindByIdDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignNoteFindByIdDelegate(service, viewModelMapper);
    }

    @Test
    void shouldReturnNoteViewModel(
            @Random CampaignNote campaignNote,
            @Random CampaignNoteViewModel noteViewModel) {

        Long campaignId = 1L;
        Long noteId = 10L;
        Long userId = 42L;

        given(service.findById(noteId, userId)).willReturn(campaignNote);
        given(viewModelMapper.apply(campaignNote)).willReturn(noteViewModel);

        CampaignNoteViewModel result = sut.findById(campaignId, noteId, userId);

        assertThat(result).isEqualTo(noteViewModel);

        var inOrder = inOrder(service, viewModelMapper);
        then(service).should(inOrder).findById(noteId, userId);
        then(viewModelMapper).should(inOrder).apply(campaignNote);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenNoteNotFound() {
        Long campaignId = 1L;
        Long noteId = 999L;
        Long userId = 42L;

        willThrow(new NotFoundException("Note not found")).given(service).findById(noteId, userId);

        assertThatThrownBy(() -> sut.findById(campaignId, noteId, userId))
                .isInstanceOf(NotFoundException.class);

        then(service).should().findById(noteId, userId);
        then(viewModelMapper).shouldHaveNoInteractions();
    }
}
