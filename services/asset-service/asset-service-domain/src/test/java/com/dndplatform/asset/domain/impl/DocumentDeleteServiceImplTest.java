package com.dndplatform.asset.domain.impl;

import com.dndplatform.asset.domain.repository.DocumentDeleteRepository;
import com.dndplatform.asset.domain.repository.DocumentMetadataDeleteRepository;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.Mockito.doThrow;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class DocumentDeleteServiceImplTest {

    @Mock
    private DocumentDeleteRepository storageRepository;

    @Mock
    private DocumentMetadataDeleteRepository metadataRepository;

    private DocumentDeleteServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentDeleteServiceImpl(storageRepository, metadataRepository);
    }

    @Test
    void shouldDeleteDocumentSuccessfully(@Random String documentId) {
        sut.delete(documentId);

        var inOrder = inOrder(storageRepository, metadataRepository);
        then(storageRepository).should(inOrder).delete(documentId);
        then(metadataRepository).should(inOrder).delete(documentId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldPropagateExceptionFromStorageRepository(@Random String documentId) {
        RuntimeException expectedException = new RuntimeException("Storage delete failed");

        doThrow(expectedException).when(storageRepository).delete(documentId);

        assertThatThrownBy(() -> sut.delete(documentId))
                .isSameAs(expectedException);

        then(storageRepository).should().delete(documentId);
        then(metadataRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldPropagateExceptionFromMetadataRepository(@Random String documentId) {
        RuntimeException expectedException = new RuntimeException("Metadata delete failed");

        doThrow(expectedException).when(metadataRepository).delete(documentId);

        assertThatThrownBy(() -> sut.delete(documentId))
                .isSameAs(expectedException);

        then(storageRepository).should().delete(documentId);
        then(metadataRepository).should().delete(documentId);
    }
}
