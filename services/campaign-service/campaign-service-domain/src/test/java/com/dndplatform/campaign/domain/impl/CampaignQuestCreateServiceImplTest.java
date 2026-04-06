package com.dndplatform.campaign.domain.impl;

import com.dndplatform.campaign.domain.CampaignQuestCreateService;
import com.dndplatform.campaign.domain.CampaignQuestCreateServiceImpl;
import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.model.CampaignQuestCreate;
import com.dndplatform.campaign.domain.repository.CampaignQuestCreateRepository;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class CampaignQuestCreateServiceImplTest {

    @Mock
    private CampaignQuestCreateRepository repository;

    private CampaignQuestCreateService sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignQuestCreateServiceImpl(repository);
    }

    @Test
    void shouldCreateQuest(@Random CampaignQuestCreate input,
                           @Random CampaignQuest expected) {
        given(repository.save(input)).willReturn(expected);

        CampaignQuest result = sut.create(input);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).save(input);
        inOrder.verifyNoMoreInteractions();
    }
}
