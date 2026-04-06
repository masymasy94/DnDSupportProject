package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignUpdate;
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
class CampaignUpdateRepositoryJpaTest {

    @Mock
    private CampaignPanacheRepository panacheRepository;
    @Mock
    private CampaignEntityMapper mapper;

    private CampaignUpdateRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignUpdateRepositoryJpa(panacheRepository, mapper);
    }

    @Test
    void shouldUpdateAndReturnMappedCampaign(@Random CampaignUpdate input, @Random Campaign expected) {
        CampaignEntity entity = new CampaignEntity();
        given(panacheRepository.findById(input.id())).willReturn(entity);
        willDoNothing().given(panacheRepository).persist(any(CampaignEntity.class));
        given(mapper.toCampaign(any(CampaignEntity.class))).willReturn(expected);

        Campaign result = sut.update(input);

        assertThat(result).isEqualTo(expected);
        then(panacheRepository).should().findById(input.id());
        then(panacheRepository).should().persist(any(CampaignEntity.class));
        then(mapper).should().toCampaign(any(CampaignEntity.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCampaignDoesNotExist(@Random CampaignUpdate input) {
        given(panacheRepository.findById(input.id())).willReturn(null);

        assertThatThrownBy(() -> sut.update(input))
                .isInstanceOf(NotFoundException.class);

        then(panacheRepository).should().findById(input.id());
        then(panacheRepository).shouldHaveNoMoreInteractions();
        then(mapper).shouldHaveNoInteractions();
    }
}
