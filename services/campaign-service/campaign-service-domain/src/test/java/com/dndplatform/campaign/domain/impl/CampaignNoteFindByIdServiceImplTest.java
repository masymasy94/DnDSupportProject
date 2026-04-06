package com.dndplatform.campaign.domain.impl;

import com.dndplatform.campaign.domain.CampaignNoteFindByIdService;
import com.dndplatform.campaign.domain.CampaignNoteFindByIdServiceImpl;
import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.model.NoteVisibility;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class CampaignNoteFindByIdServiceImplTest {

    @Mock
    private CampaignNoteFindByIdRepository repository;

    private CampaignNoteFindByIdService sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignNoteFindByIdServiceImpl(repository);
    }

    @Test
    void shouldReturnNoteWhenFoundAndAccessible(@Random Long noteId,
                                                 @Random Long userId) {
        CampaignNote expected = new CampaignNote(noteId, 1L, userId, "Title", "Content", NoteVisibility.PUBLIC, null, null);
        given(repository.findById(noteId)).willReturn(Optional.of(expected));

        CampaignNote result = sut.findById(noteId, userId);

        assertThat(result).isEqualTo(expected);
        then(repository).should().findById(noteId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenNoteNotFound(@Random Long noteId,
                                                       @Random Long userId) {
        given(repository.findById(noteId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.findById(noteId, userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Note not found");
    }

    @Test
    void shouldThrowForbiddenExceptionWhenAccessingPrivateNoteAsNonAuthor(@Random Long noteId,
                                                                           @Random Long userId) {
        var note = new CampaignNote(noteId, 1L, 999L, "Title", "Content", NoteVisibility.PRIVATE, null, null);
        given(repository.findById(noteId)).willReturn(Optional.of(note));

        assertThatThrownBy(() -> sut.findById(noteId, userId))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("private note");
    }
}
