package com.dndplatform.asset.domain.impl;

import com.dndplatform.asset.domain.event.DocumentUploadedEventPublisher;
import com.dndplatform.asset.domain.model.Document;
import com.dndplatform.asset.domain.model.DocumentContent;
import com.dndplatform.asset.domain.repository.DocumentMetadataCreateRepository;
import com.dndplatform.asset.domain.repository.DocumentUploadRepository;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class DocumentUploadBatchServiceImplTest {

    @Mock
    private DocumentUploadRepository repository;

    @Mock
    private DocumentMetadataCreateRepository metadataRepository;

    @Mock
    private DocumentUploadedEventPublisher eventPublisher;

    private DocumentUploadBatchServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentUploadBatchServiceImpl(repository, metadataRepository, eventPublisher);
    }

    @Test
    void shouldUploadMultipleDocumentsSuccessfully(
            @Random String uploadedBy,
            @Random DocumentContent doc1,
            @Random DocumentContent doc2,
            @Random Document uploadedDoc1,
            @Random Document uploadedDoc2) {

        given(repository.upload(doc1.fileName(), doc1.contentType(), doc1.inputStream(), doc1.size(), uploadedBy))
                .willReturn(uploadedDoc1);
        given(repository.upload(doc2.fileName(), doc2.contentType(), doc2.inputStream(), doc2.size(), uploadedBy))
                .willReturn(uploadedDoc2);

        List<Document> result = sut.uploadMultiple(List.of(doc1, doc2), uploadedBy);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(uploadedDoc1, uploadedDoc2);

        var inOrder = inOrder(repository, metadataRepository, eventPublisher);
        then(repository).should(inOrder).upload(doc1.fileName(), doc1.contentType(), doc1.inputStream(), doc1.size(), uploadedBy);
        then(metadataRepository).should(inOrder).save(uploadedDoc1);
        then(eventPublisher).should(inOrder).publish(uploadedDoc1);
        then(repository).should(inOrder).upload(doc2.fileName(), doc2.contentType(), doc2.inputStream(), doc2.size(), uploadedBy);
        then(metadataRepository).should(inOrder).save(uploadedDoc2);
        then(eventPublisher).should(inOrder).publish(uploadedDoc2);
    }

    @Test
    void shouldReturnEmptyListWhenNoDocumentsProvided(@Random String uploadedBy) {
        List<Document> result = sut.uploadMultiple(List.of(), uploadedBy);

        assertThat(result).isEmpty();

        then(repository).shouldHaveNoInteractions();
        then(metadataRepository).shouldHaveNoInteractions();
        then(eventPublisher).shouldHaveNoInteractions();
    }

    @Test
    void shouldPropagateExceptionFromRepository(
            @Random String uploadedBy,
            @Random DocumentContent doc1) {

        RuntimeException expectedException = new RuntimeException("Upload failed");

        given(repository.upload(doc1.fileName(), doc1.contentType(), doc1.inputStream(), doc1.size(), uploadedBy))
                .willThrow(expectedException);

        assertThatThrownBy(() ->
                sut.uploadMultiple(List.of(doc1), uploadedBy))
                .isSameAs(expectedException);

        then(repository).should().upload(doc1.fileName(), doc1.contentType(), doc1.inputStream(), doc1.size(), uploadedBy);
    }
}
