package com.dndplatform.asset.adapter.outbound.jpa.repository;

import com.dndplatform.asset.adapter.outbound.jpa.entity.DocumentMetadataEntity;
import com.dndplatform.asset.domain.model.Document;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class DocumentMetadataCreateRepositoryJpaTest {

    @Mock
    private DocumentMetadataPanacheRepository panacheRepository;

    private DocumentMetadataCreateRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentMetadataCreateRepositoryJpa(panacheRepository);
    }

    @Test
    void shouldPersistEntityWithDocumentFields(@Random Document document) {
        willDoNothing().given(panacheRepository).persist(any(DocumentMetadataEntity.class));
        var captor = ArgumentCaptor.forClass(DocumentMetadataEntity.class);

        sut.save(document);

        then(panacheRepository).should().persist(captor.capture());
        var entity = captor.getValue();
        assertThat(entity.id).isEqualTo(document.id());
        assertThat(entity.fileName).isEqualTo(document.fileName());
        assertThat(entity.contentType).isEqualTo(document.contentType());
        assertThat(entity.size).isEqualTo(document.size());
        assertThat(entity.uploadedBy).isEqualTo(document.uploadedBy());
        assertThat(entity.ragStatus).isEqualTo("PENDING");
    }

    @Test
    void shouldSetUploadedAtFromDocumentWhenPresent(@Random Document document) {
        willDoNothing().given(panacheRepository).persist(any(DocumentMetadataEntity.class));
        var captor = ArgumentCaptor.forClass(DocumentMetadataEntity.class);

        sut.save(document);

        then(panacheRepository).should().persist(captor.capture());
        assertThat(captor.getValue().uploadedAt).isNotNull();
    }
}
