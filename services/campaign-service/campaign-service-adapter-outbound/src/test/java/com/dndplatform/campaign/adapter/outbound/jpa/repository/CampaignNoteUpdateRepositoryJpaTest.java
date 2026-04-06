package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignNoteEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.model.CampaignNoteUpdate;
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
class CampaignNoteUpdateRepositoryJpaTest {

    @Mock
    private CampaignNotePanacheRepository panacheRepository;
    @Mock
    private CampaignEntityMapper mapper;

    private CampaignNoteUpdateRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignNoteUpdateRepositoryJpa(panacheRepository, mapper);
    }

    @Test
    void shouldUpdateAndReturnMappedNote(@Random CampaignNoteUpdate input, @Random CampaignNote expected) {
        CampaignNoteEntity entity = new CampaignNoteEntity();
        given(panacheRepository.findById(input.id())).willReturn(entity);
        willDoNothing().given(panacheRepository).persist(any(CampaignNoteEntity.class));
        given(mapper.toCampaignNote(any(CampaignNoteEntity.class))).willReturn(expected);

        CampaignNote result = sut.update(input);

        assertThat(result).isEqualTo(expected);
        then(panacheRepository).should().findById(input.id());
        then(panacheRepository).should().persist(any(CampaignNoteEntity.class));
        then(mapper).should().toCampaignNote(any(CampaignNoteEntity.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenNoteDoesNotExist(@Random CampaignNoteUpdate input) {
        given(panacheRepository.findById(input.id())).willReturn(null);

        assertThatThrownBy(() -> sut.update(input))
                .isInstanceOf(NotFoundException.class);

        then(panacheRepository).should().findById(input.id());
        then(panacheRepository).shouldHaveNoMoreInteractions();
        then(mapper).shouldHaveNoInteractions();
    }
}
