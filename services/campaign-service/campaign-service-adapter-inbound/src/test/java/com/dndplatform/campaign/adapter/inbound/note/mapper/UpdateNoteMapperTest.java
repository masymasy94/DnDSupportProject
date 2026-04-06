package com.dndplatform.campaign.adapter.inbound.note.mapper;

import com.dndplatform.campaign.domain.model.NoteVisibility;
import com.dndplatform.campaign.view.model.vm.UpdateNoteRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateNoteMapperTest {

    private UpdateNoteMapper sut;

    @BeforeEach
    void setUp() {
        sut = new UpdateNoteMapper();
    }

    @Test
    void shouldMapAllFields() {
        var request = new UpdateNoteRequest(1L, "Updated Title", "Updated content", "PUBLIC");

        var result = sut.apply(5L, request);

        assertThat(result.id()).isEqualTo(5L);
        assertThat(result.title()).isEqualTo("Updated Title");
        assertThat(result.content()).isEqualTo("Updated content");
        assertThat(result.visibility()).isEqualTo(NoteVisibility.PUBLIC);
    }

    @Test
    void shouldMapNullVisibilityToNull() {
        var request = new UpdateNoteRequest(1L, "Title", "Content", null);

        var result = sut.apply(5L, request);

        assertThat(result.visibility()).isNull();
    }
}
