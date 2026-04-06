package com.dndplatform.campaign.domain.impl;

import com.dndplatform.campaign.domain.CampaignFindByIdService;
import com.dndplatform.campaign.domain.CampaignFindByIdServiceImpl;
import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.repository.CampaignFindByIdRepository;
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
class CampaignFindByIdServiceImplTest {

    @Mock
    private CampaignFindByIdRepository repository;

    private CampaignFindByIdService sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignFindByIdServiceImpl(repository);
    }

    @Test
    void shouldReturnCampaignWhenFound(@Random Campaign expected,
                                      @Random Long id) {
        given(repository.findById(id)).willReturn(Optional.of(expected));

        Campaign result = sut.findById(id);

        assertThat(result).isEqualTo(expected);
        then(repository).should().findById(id);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenNotFound(@Random Long id) {
        given(repository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.findById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Campaign not found");

        then(repository).should().findById(id);
    }
}
