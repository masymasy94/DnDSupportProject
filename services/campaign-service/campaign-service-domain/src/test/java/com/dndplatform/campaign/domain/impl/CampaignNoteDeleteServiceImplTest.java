package com.dndplatform.campaign.domain.impl;

import com.dndplatform.campaign.domain.CampaignNoteDeleteService;
import com.dndplatform.campaign.domain.CampaignNoteDeleteServiceImpl;
import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.model.CampaignStatus;
import com.dndplatform.campaign.domain.model.NoteVisibility;
import com.dndplatform.campaign.domain.repository.CampaignFindByIdRepository;
import com.dndplatform.campaign.domain.repository.CampaignNoteDeleteRepository;
import com.dndplatform.campaign.domain.repository.CampaignNoteFindByIdRepository;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class CampaignNoteDeleteServiceImplTest {

    @Mock
    private CampaignFindByIdRepository campaignFindRepository;
    @Mock
    private CampaignNoteFindByIdRepository noteFindByIdRepository;
    @Mock
    private CampaignNoteDeleteRepository noteDeleteRepository;

    private CampaignNoteDeleteService sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignNoteDeleteServiceImpl(campaignFindRepository, noteFindByIdRepository, noteDeleteRepository);
    }

    @Test
    void shouldDeleteNoteWhenUserIsDungeonMaster(@Random CampaignNote note,
                                                  @Random Campaign campaign,
                                                  @Random Long campaignId,
                                                  @Random Long noteId,
                                                  @Random Long userId) {
        campaign = new Campaign(campaignId, "Test", "Desc", userId, CampaignStatus.ACTIVE, 5, null, null, null);
        given(noteFindByIdRepository.findById(noteId)).willReturn(Optional.of(note));
        given(campaignFindRepository.findById(campaignId)).willReturn(Optional.of(campaign));

        sut.delete(campaignId, noteId, userId);

        var inOrder = inOrder(noteFindByIdRepository, campaignFindRepository, noteDeleteRepository);
        then(noteFindByIdRepository).should(inOrder).findById(noteId);
        then(campaignFindRepository).should(inOrder).findById(campaignId);
        then(noteDeleteRepository).should(inOrder).deleteById(noteId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldDeleteNoteWhenUserIsAuthor(@Random CampaignNote note,
                                           @Random Campaign campaign,
                                           @Random Long campaignId,
                                           @Random Long noteId,
                                           @Random Long userId) {
        note = new CampaignNote(noteId, campaignId, userId, "Title", "Content", NoteVisibility.PRIVATE, null, null);
        campaign = new Campaign(campaignId, "Test", "Desc", 999L, CampaignStatus.ACTIVE, 5, null, null, null);
        given(noteFindByIdRepository.findById(noteId)).willReturn(Optional.of(note));
        given(campaignFindRepository.findById(campaignId)).willReturn(Optional.of(campaign));

        sut.delete(campaignId, noteId, userId);

        var inOrder = inOrder(noteFindByIdRepository, campaignFindRepository, noteDeleteRepository);
        then(noteFindByIdRepository).should(inOrder).findById(noteId);
        then(campaignFindRepository).should(inOrder).findById(campaignId);
        then(noteDeleteRepository).should(inOrder).deleteById(noteId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenNoteDoesNotExist(@Random Long campaignId,
                                                           @Random Long noteId,
                                                           @Random Long userId) {
        given(noteFindByIdRepository.findById(noteId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.delete(campaignId, noteId, userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Note not found");

        then(campaignFindRepository).shouldHaveNoInteractions();
        then(noteDeleteRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowForbiddenExceptionWhenUserIsNotAuthorOrDm(@Random CampaignNote note,
                                                               @Random Campaign campaign,
                                                               @Random Long campaignId,
                                                               @Random Long noteId,
                                                               @Random Long userId) {
        note = new CampaignNote(noteId, campaignId, 999L, "Title", "Content", NoteVisibility.PRIVATE, null, null);
        campaign = new Campaign(campaignId, "Test", "Desc", 888L, CampaignStatus.ACTIVE, 5, null, null, null);
        given(noteFindByIdRepository.findById(noteId)).willReturn(Optional.of(note));
        given(campaignFindRepository.findById(campaignId)).willReturn(Optional.of(campaign));

        assertThatThrownBy(() -> sut.delete(campaignId, noteId, userId))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("author or Dungeon Master");

        then(noteDeleteRepository).shouldHaveNoInteractions();
    }
}
