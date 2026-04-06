package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignNoteEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CampaignNoteFindByIdRepositoryJpaTest {

    @Mock
    private CampaignNotePanacheRepository panacheRepository;
    @Mock
    private CampaignEntityMapper mapper;

    private CampaignNoteFindByIdRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignNoteFindByIdRepositoryJpa(panacheRepository, mapper);
    }

    @Test
    void shouldReturnMappedNoteWhenFound(@Random CampaignNote expected) {
        Long noteId = 1L;
        CampaignNoteEntity entity = new CampaignNoteEntity();
        given(panacheRepository.findByIdOptional(noteId)).willReturn(Optional.of(entity));
        given(mapper.toCampaignNote(entity)).willReturn(expected);

        Optional<CampaignNote> result = sut.findById(noteId);

        assertThat(result).isPresent().contains(expected);
        then(panacheRepository).should().findByIdOptional(noteId);
        then(mapper).should().toCampaignNote(entity);
    }

    @Test
    void shouldReturnEmptyWhenNoteNotFound() {
        Long noteId = 99L;
        given(panacheRepository.findByIdOptional(noteId)).willReturn(Optional.empty());

        Optional<CampaignNote> result = sut.findById(noteId);

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
