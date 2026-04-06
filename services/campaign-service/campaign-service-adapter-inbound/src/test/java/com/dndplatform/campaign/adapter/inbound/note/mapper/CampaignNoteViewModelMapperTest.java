package com.dndplatform.campaign.adapter.inbound.note.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.campaign.domain.model.CampaignNote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class CampaignNoteViewModelMapperTest {

    private CampaignNoteViewModelMapper sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignNoteViewModelMapper();
    }

    @Test
    void shouldMapAllFields(@Random CampaignNote note) {
        var result = sut.apply(note);

        assertThat(result.id()).isEqualTo(note.id());
        assertThat(result.campaignId()).isEqualTo(note.campaignId());
        assertThat(result.authorId()).isEqualTo(note.authorId());
        assertThat(result.title()).isEqualTo(note.title());
        assertThat(result.content()).isEqualTo(note.content());
        assertThat(result.visibility()).isEqualTo(note.visibility().name());
        assertThat(result.createdAt()).isEqualTo(note.createdAt());
        assertThat(result.updatedAt()).isEqualTo(note.updatedAt());
    }
}
