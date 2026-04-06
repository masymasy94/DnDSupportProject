package com.dndplatform.asset.adapter.outbound.jpa.repository;

import com.dndplatform.asset.adapter.outbound.jpa.entity.DocumentMetadataEntity;
import com.dndplatform.asset.domain.model.RagStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class DocumentMetadataUpdateRagStatusRepositoryJpaTest {

    @Mock
    private DocumentMetadataPanacheRepository panacheRepository;

    private DocumentMetadataUpdateRagStatusRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentMetadataUpdateRagStatusRepositoryJpa(panacheRepository);
    }

    @Test
    void shouldUpdateRagStatusAndErrorMessage() {
        var entity = new DocumentMetadataEntity();
        given(panacheRepository.findById("doc-1")).willReturn(entity);

        sut.updateRagStatus("doc-1", RagStatus.FAILED, "parse error");

        assertThat(entity.ragStatus).isEqualTo("FAILED");
        assertThat(entity.ragErrorMessage).isEqualTo("parse error");
        assertThat(entity.ragProcessedAt).isNotNull();
    }

    @Test
    void shouldSetRagProcessedAtForCompletedStatus() {
        var entity = new DocumentMetadataEntity();
        given(panacheRepository.findById("doc-2")).willReturn(entity);

        sut.updateRagStatus("doc-2", RagStatus.COMPLETED, null);

        assertThat(entity.ragStatus).isEqualTo("COMPLETED");
        assertThat(entity.ragProcessedAt).isNotNull();
    }

    @Test
    void shouldNotSetRagProcessedAtForProcessingStatus() {
        var entity = new DocumentMetadataEntity();
        given(panacheRepository.findById("doc-3")).willReturn(entity);

        sut.updateRagStatus("doc-3", RagStatus.PROCESSING, null);

        assertThat(entity.ragStatus).isEqualTo("PROCESSING");
        assertThat(entity.ragProcessedAt).isNull();
    }

    @Test
    void shouldDoNothingWhenEntityNotFound() {
        given(panacheRepository.findById("doc-404")).willReturn(null);

        sut.updateRagStatus("doc-404", RagStatus.COMPLETED, null);

        then(panacheRepository).should().findById("doc-404");
        then(panacheRepository).shouldHaveNoMoreInteractions();
    }
}
