package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignQuestEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.model.CampaignQuestUpdate;
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
class CampaignQuestUpdateRepositoryJpaTest {

    @Mock
    private CampaignQuestPanacheRepository panacheRepository;
    @Mock
    private CampaignEntityMapper mapper;

    private CampaignQuestUpdateRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignQuestUpdateRepositoryJpa(panacheRepository, mapper);
    }

    @Test
    void shouldUpdateAndReturnMappedQuest(@Random CampaignQuestUpdate input, @Random CampaignQuest expected) {
        CampaignQuestEntity entity = new CampaignQuestEntity();
        given(panacheRepository.findById(input.id())).willReturn(entity);
        willDoNothing().given(panacheRepository).persist(any(CampaignQuestEntity.class));
        given(mapper.toCampaignQuest(any(CampaignQuestEntity.class))).willReturn(expected);

        CampaignQuest result = sut.update(input);

        assertThat(result).isEqualTo(expected);
        then(panacheRepository).should().findById(input.id());
        then(panacheRepository).should().persist(any(CampaignQuestEntity.class));
        then(mapper).should().toCampaignQuest(any(CampaignQuestEntity.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenQuestDoesNotExist(@Random CampaignQuestUpdate input) {
        given(panacheRepository.findById(input.id())).willReturn(null);

        assertThatThrownBy(() -> sut.update(input))
                .isInstanceOf(NotFoundException.class);

        then(panacheRepository).should().findById(input.id());
        then(panacheRepository).shouldHaveNoMoreInteractions();
        then(mapper).shouldHaveNoInteractions();
    }
}
