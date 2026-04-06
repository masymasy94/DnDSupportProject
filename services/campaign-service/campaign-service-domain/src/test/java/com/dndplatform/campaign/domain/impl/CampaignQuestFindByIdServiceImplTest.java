package com.dndplatform.campaign.domain.impl;

import com.dndplatform.campaign.domain.CampaignQuestFindByIdService;
import com.dndplatform.campaign.domain.CampaignQuestFindByIdServiceImpl;
import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.repository.CampaignQuestFindByIdRepository;
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
class CampaignQuestFindByIdServiceImplTest {

    @Mock
    private CampaignQuestFindByIdRepository repository;

    private CampaignQuestFindByIdService sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignQuestFindByIdServiceImpl(repository);
    }

    @Test
    void shouldReturnQuestWhenFound(@Random CampaignQuest expected,
                                     @Random Long questId) {
        given(repository.findById(questId)).willReturn(Optional.of(expected));

        CampaignQuest result = sut.findById(questId);

        assertThat(result).isEqualTo(expected);
        then(repository).should().findById(questId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenNotFound(@Random Long questId) {
        given(repository.findById(questId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.findById(questId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Quest not found");
    }
}
