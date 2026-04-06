package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignNoteEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.model.CampaignNoteCreate;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CampaignNoteCreateRepositoryJpaTest {

    @Mock
    private CampaignPanacheRepository campaignPanacheRepository;
    @Mock
    private CampaignNotePanacheRepository notePanacheRepository;
    @Mock
    private CampaignEntityMapper mapper;

    private CampaignNoteCreateRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignNoteCreateRepositoryJpa(campaignPanacheRepository, notePanacheRepository, mapper);
    }

    @Test
    void shouldPersistNoteAndReturnMapped(@Random CampaignNoteCreate input, @Random CampaignNote expected) {
        CampaignEntity campaign = new CampaignEntity();
        given(campaignPanacheRepository.findById(input.campaignId())).willReturn(campaign);
        willDoNothing().given(notePanacheRepository).persist(any(CampaignNoteEntity.class));
        given(mapper.toCampaignNote(any(CampaignNoteEntity.class))).willReturn(expected);

        CampaignNote result = sut.save(input);

        assertThat(result).isEqualTo(expected);
        then(campaignPanacheRepository).should().findById(input.campaignId());
        then(notePanacheRepository).should().persist(any(CampaignNoteEntity.class));
        then(mapper).should().toCampaignNote(any(CampaignNoteEntity.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCampaignDoesNotExist(@Random CampaignNoteCreate input) {
        given(campaignPanacheRepository.findById(input.campaignId())).willReturn(null);

        assertThatThrownBy(() -> sut.save(input))
                .isInstanceOf(NotFoundException.class);

        then(notePanacheRepository).shouldHaveNoInteractions();
        then(mapper).shouldHaveNoInteractions();
    }
}
