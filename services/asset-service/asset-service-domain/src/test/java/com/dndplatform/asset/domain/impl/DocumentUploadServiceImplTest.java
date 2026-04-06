package com.dndplatform.asset.domain.impl;

import com.dndplatform.asset.domain.event.DocumentUploadedEventPublisher;
import com.dndplatform.asset.domain.model.Document;
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

import java.io.ByteArrayInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class DocumentUploadServiceImplTest {

    @Mock
    private DocumentUploadRepository repository;

    @Mock
    private DocumentMetadataCreateRepository metadataRepository;

    @Mock
    private DocumentUploadedEventPublisher eventPublisher;

    private DocumentUploadServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentUploadServiceImpl(repository, metadataRepository, eventPublisher);
    }

    @Test
    void shouldUploadDocumentSuccessfully(
            @Random String fileName,
            @Random String contentType,
            @Random ByteArrayInputStream inputStream,
            @Random long size,
            @Random String uploadedBy,
            @Random Document uploadedDocument) {

        given(repository.upload(fileName, contentType, inputStream, size, uploadedBy))
                .willReturn(uploadedDocument);

        Document result = sut.upload(fileName, contentType, inputStream, size, uploadedBy);

        assertThat(result).isEqualTo(uploadedDocument);

        then(repository).should().upload(fileName, contentType, inputStream, size, uploadedBy);
        then(metadataRepository).should().save(uploadedDocument);
        then(eventPublisher).should().publish(uploadedDocument);
    }

    @Test
    void shouldPropagateExceptionFromRepository(
            @Random String fileName,
            @Random String contentType,
            @Random ByteArrayInputStream inputStream,
            @Random long size,
            @Random String uploadedBy) {

        RuntimeException expectedException = new RuntimeException("Upload failed");

        given(repository.upload(fileName, contentType, inputStream, size, uploadedBy))
                .willThrow(expectedException);

        assertThatThrownBy(() ->
                sut.upload(fileName, contentType, inputStream, size, uploadedBy))
                .isSameAs(expectedException);

        then(repository).should().upload(fileName, contentType, inputStream, size, uploadedBy);
        then(metadataRepository).shouldHaveNoInteractions();
        then(eventPublisher).shouldHaveNoInteractions();
    }
}
