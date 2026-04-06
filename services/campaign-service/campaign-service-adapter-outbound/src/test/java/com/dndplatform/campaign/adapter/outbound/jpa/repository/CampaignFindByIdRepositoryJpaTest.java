package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.Campaign;
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
class CampaignFindByIdRepositoryJpaTest {

    @Mock
    private CampaignPanacheRepository panacheRepository;
    @Mock
    private CampaignEntityMapper mapper;

    private CampaignFindByIdRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignFindByIdRepositoryJpa(panacheRepository, mapper);
    }

    @Test
    void shouldReturnMappedCampaignWhenFound(@Random Campaign expected) {
        Long id = 1L;
        CampaignEntity entity = new CampaignEntity();
        given(panacheRepository.findByIdOptional(id)).willReturn(Optional.of(entity));
        given(mapper.toCampaign(entity)).willReturn(expected);

        Optional<Campaign> result = sut.findById(id);

        assertThat(result).isPresent().contains(expected);
        then(panacheRepository).should().findByIdOptional(id);
        then(mapper).should().toCampaign(entity);
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        Long id = 99L;
        given(panacheRepository.findByIdOptional(id)).willReturn(Optional.empty());

        Optional<Campaign> result = sut.findById(id);

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
