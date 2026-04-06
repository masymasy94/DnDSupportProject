package com.dndplatform.campaign.domain.impl;

import com.dndplatform.campaign.domain.CampaignNoteUpdateService;
import com.dndplatform.campaign.domain.CampaignNoteUpdateServiceImpl;
import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.model.CampaignNoteUpdate;
import com.dndplatform.campaign.domain.repository.CampaignNoteFindByIdRepository;
import com.dndplatform.campaign.domain.repository.CampaignNoteUpdateRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class CampaignNoteUpdateServiceImplTest {

    @Mock
    private CampaignNoteFindByIdRepository noteFindByIdRepository;
    @Mock
    private CampaignNoteUpdateRepository noteUpdateRepository;

    private CampaignNoteUpdateService sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignNoteUpdateServiceImpl(noteFindByIdRepository, noteUpdateRepository);
    }

    @Test
    void shouldUpdateNoteWhenUserIsAuthor(@Random CampaignNoteUpdate input,
                                           @Random CampaignNote existing,
                                           @Random CampaignNote expected,
                                           @Random Long userId) {
        existing = new CampaignNote(input.id(), 1L, userId, "Old Title", "Old Content", null, null, null);
        given(noteFindByIdRepository.findById(input.id())).willReturn(Optional.of(existing));
        given(noteUpdateRepository.update(input)).willReturn(expected);

        CampaignNote result = sut.update(input, userId);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(noteFindByIdRepository, noteUpdateRepository);
        then(noteFindByIdRepository).should(inOrder).findById(input.id());
        then(noteUpdateRepository).should(inOrder).update(input);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenNoteDoesNotExist(@Random CampaignNoteUpdate input,
                                                           @Random Long userId) {
        given(noteFindByIdRepository.findById(input.id())).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.update(input, userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Note not found");

        then(noteUpdateRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowForbiddenExceptionWhenUserIsNotAuthor(@Random CampaignNoteUpdate input,
                                                           @Random CampaignNote existing,
                                                           @Random Long userId) {
        existing = new CampaignNote(input.id(), 1L, 999L, "Old Title", "Old Content", null, null, null);
        given(noteFindByIdRepository.findById(input.id())).willReturn(Optional.of(existing));

        assertThatThrownBy(() -> sut.update(input, userId))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("author");

        then(noteUpdateRepository).shouldHaveNoInteractions();
    }
}
