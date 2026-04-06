package com.dndplatform.campaign.adapter.inbound.note;

import com.dndplatform.campaign.adapter.inbound.note.mapper.CampaignNoteViewModelMapper;
import com.dndplatform.campaign.adapter.inbound.note.mapper.CreateNoteMapper;
import com.dndplatform.campaign.domain.CampaignNoteCreateService;
import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.model.CampaignNoteCreate;
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
class CampaignNoteCreateDelegateTest {

    @Mock
    private CampaignNoteCreateService service;

    @Mock
    private CampaignNoteViewModelMapper viewModelMapper;

    @Mock
    private CreateNoteMapper createMapper;

    private CampaignNoteCreateDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignNoteCreateDelegate(service, viewModelMapper, createMapper);
    }

    @Test
    void shouldReturnNoteViewModel(
            @Random CampaignNoteCreate campaignNoteCreate,
            @Random CampaignNote campaignNote,
            @Random CampaignNoteViewModel noteViewModel) {

        Long campaignId = 1L;
        Long userId = 42L;
        CreateNoteRequest request = new CreateNoteRequest(userId, "Session Notes", "The party traveled north...", "PUBLIC");

        given(createMapper.apply(campaignId, request, userId)).willReturn(campaignNoteCreate);
        given(service.create(campaignNoteCreate)).willReturn(campaignNote);
        given(viewModelMapper.apply(campaignNote)).willReturn(noteViewModel);

        CampaignNoteViewModel result = sut.create(campaignId, request);

        assertThat(result).isEqualTo(noteViewModel);

        var inOrder = inOrder(createMapper, service, viewModelMapper);
        then(createMapper).should(inOrder).apply(campaignId, request, userId);
        then(service).should(inOrder).create(campaignNoteCreate);
        then(viewModelMapper).should(inOrder).apply(campaignNote);
        inOrder.verifyNoMoreInteractions();
    }
}
