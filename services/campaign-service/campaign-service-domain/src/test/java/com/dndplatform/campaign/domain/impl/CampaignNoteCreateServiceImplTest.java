package com.dndplatform.campaign.domain.impl;

import com.dndplatform.campaign.domain.CampaignNoteCreateService;
import com.dndplatform.campaign.domain.CampaignNoteCreateServiceImpl;
import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.model.CampaignNoteCreate;
import com.dndplatform.campaign.domain.repository.CampaignNoteCreateRepository;
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
class CampaignNoteCreateServiceImplTest {

    @Mock
    private CampaignNoteCreateRepository repository;

    private CampaignNoteCreateService sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignNoteCreateServiceImpl(repository);
    }

    @Test
    void shouldCreateNote(@Random CampaignNoteCreate input,
                          @Random CampaignNote expected) {
        given(repository.save(input)).willReturn(expected);

        CampaignNote result = sut.create(input);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).save(input);
        inOrder.verifyNoMoreInteractions();
    }
}
