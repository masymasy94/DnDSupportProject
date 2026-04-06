package com.dndplatform.campaign.adapter.inbound.note.mapper;

import com.dndplatform.campaign.domain.model.NoteVisibility;
import com.dndplatform.campaign.view.model.vm.CreateNoteRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateNoteMapperTest {

    private CreateNoteMapper sut;

    @BeforeEach
    void setUp() {
        sut = new CreateNoteMapper();
    }

    @Test
    void shouldMapAllFields() {
        var request = new CreateNoteRequest(1L, "Session Notes", "Content here", "PRIVATE");

        var result = sut.apply(10L, request, 42L);

        assertThat(result.campaignId()).isEqualTo(10L);
        assertThat(result.authorId()).isEqualTo(42L);
        assertThat(result.title()).isEqualTo("Session Notes");
        assertThat(result.content()).isEqualTo("Content here");
        assertThat(result.visibility()).isEqualTo(NoteVisibility.PRIVATE);
    }

    @Test
    void shouldDefaultVisibilityToPublicWhenNull() {
        var request = new CreateNoteRequest(1L, "Session Notes", "Content", null);

        var result = sut.apply(10L, request, 42L);

        assertThat(result.visibility()).isEqualTo(NoteVisibility.PUBLIC);
    }
}
