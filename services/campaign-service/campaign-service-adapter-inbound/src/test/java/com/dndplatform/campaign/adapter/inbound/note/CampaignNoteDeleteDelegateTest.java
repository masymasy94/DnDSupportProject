package com.dndplatform.campaign.adapter.inbound.note;

import com.dndplatform.campaign.domain.CampaignNoteDeleteService;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CampaignNoteDeleteDelegateTest {

    @Mock
    private CampaignNoteDeleteService service;

    private CampaignNoteDeleteDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignNoteDeleteDelegate(service);
    }

    @Test
    void shouldDelegateToService() {
        Long campaignId = 1L;
        Long noteId = 10L;
        Long userId = 42L;

        willDoNothing().given(service).delete(campaignId, noteId, userId);

        sut.delete(campaignId, noteId, userId);

        var inOrder = inOrder(service);
        then(service).should(inOrder).delete(campaignId, noteId, userId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenUserIsNotAuthorized() {
        Long campaignId = 1L;
        Long noteId = 10L;
        Long userId = 99L;

        willThrow(new ForbiddenException("Not the note author")).given(service).delete(campaignId, noteId, userId);

        assertThatThrownBy(() -> sut.delete(campaignId, noteId, userId))
                .isInstanceOf(ForbiddenException.class);

        then(service).should().delete(campaignId, noteId, userId);
    }
}
