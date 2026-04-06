package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignNoteEntity;
import com.dndplatform.common.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class CampaignNoteDeleteRepositoryJpaTest {

    @Mock
    private CampaignNotePanacheRepository panacheRepository;

    private CampaignNoteDeleteRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignNoteDeleteRepositoryJpa(panacheRepository);
    }

    @Test
    void shouldFindAndDeleteNote() {
        Long noteId = 1L;
        CampaignNoteEntity entity = new CampaignNoteEntity();
        given(panacheRepository.findById(noteId)).willReturn(entity);
        willDoNothing().given(panacheRepository).delete(entity);

        sut.deleteById(noteId);

        then(panacheRepository).should().findById(noteId);
        then(panacheRepository).should().delete(entity);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenNoteDoesNotExist() {
        Long noteId = 99L;
        given(panacheRepository.findById(noteId)).willReturn(null);

        assertThatThrownBy(() -> sut.deleteById(noteId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");

        then(panacheRepository).should().findById(noteId);
        then(panacheRepository).shouldHaveNoMoreInteractions();
    }
}
