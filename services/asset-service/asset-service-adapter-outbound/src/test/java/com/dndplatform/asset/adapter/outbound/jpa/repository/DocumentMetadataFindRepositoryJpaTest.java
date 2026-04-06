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
class DocumentMetadataFindRepositoryJpaTest {

    @Mock
    private DocumentMetadataPanacheRepository panacheRepository;

    private DocumentMetadataFindRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentMetadataFindRepositoryJpa(panacheRepository);
    }

    @Test
    void shouldReturnRagStatusWhenEntityFound() {
        var entity = new DocumentMetadataEntity();
        entity.ragStatus = "COMPLETED";
        given(panacheRepository.findById("doc-1")).willReturn(entity);

        var result = sut.findRagStatus("doc-1");

        assertThat(result).contains(RagStatus.COMPLETED);
        then(panacheRepository).should().findById("doc-1");
    }

    @Test
    void shouldReturnEmptyWhenEntityNotFound() {
        given(panacheRepository.findById("doc-404")).willReturn(null);

        var result = sut.findRagStatus("doc-404");

        assertThat(result).isEmpty();
    }
}
