package com.dndplatform.campaign.domain.impl;

import com.dndplatform.campaign.domain.CampaignNoteFindVisibleService;
import com.dndplatform.campaign.domain.CampaignNoteFindVisibleServiceImpl;
import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.repository.CampaignNoteFindVisibleRepository;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class CampaignNoteFindVisibleServiceImplTest {

    @Mock
    private CampaignNoteFindVisibleRepository repository;

    private CampaignNoteFindVisibleService sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignNoteFindVisibleServiceImpl(repository);
    }

    @Test
    void shouldReturnVisibleNotes(@Random CampaignNote note1,
                                   @Random CampaignNote note2,
                                   @Random Long campaignId,
                                   @Random Long userId) {
        List<CampaignNote> expected = List.of(note1, note2);
        given(repository.findVisibleNotes(campaignId, userId)).willReturn(expected);

        List<CampaignNote> result = sut.findVisibleNotes(campaignId, userId);

        assertThat(result).isEqualTo(expected);
        then(repository).should().findVisibleNotes(campaignId, userId);
    }
}
