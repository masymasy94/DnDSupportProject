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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CampaignNoteFindVisibleRepositoryJpaTest {

    @Mock
    private CampaignNotePanacheRepository panacheRepository;
    @Mock
    private CampaignEntityMapper mapper;

    private CampaignNoteFindVisibleRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignNoteFindVisibleRepositoryJpa(panacheRepository, mapper);
    }

    @Test
    void shouldReturnMappedVisibleNotes(@Random CampaignNote expected) {
        Long campaignId = 1L;
        Long userId = 2L;
        CampaignNoteEntity entity = new CampaignNoteEntity();

        given(panacheRepository.findVisibleNotes(campaignId, userId)).willReturn(List.of(entity));
        given(mapper.toCampaignNote(entity)).willReturn(expected);

        List<CampaignNote> result = sut.findVisibleNotes(campaignId, userId);

        assertThat(result).containsExactly(expected);
        then(panacheRepository).should().findVisibleNotes(campaignId, userId);
        then(mapper).should().toCampaignNote(entity);
    }

    @Test
    void shouldReturnEmptyListWhenNoVisibleNotes() {
        Long campaignId = 1L;
        Long userId = 2L;
        given(panacheRepository.findVisibleNotes(campaignId, userId)).willReturn(List.of());

        List<CampaignNote> result = sut.findVisibleNotes(campaignId, userId);

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
