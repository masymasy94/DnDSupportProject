package com.dndplatform.asset.domain.impl;

import com.dndplatform.asset.domain.model.DocumentContent;
import com.dndplatform.asset.domain.repository.DocumentDownloadRepository;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class DocumentDownloadServiceImplTest {

    @Mock
    private DocumentDownloadRepository repository;

    private DocumentDownloadServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentDownloadServiceImpl(repository);
    }

    @Test
    void shouldDownloadDocumentSuccessfully(@Random String documentId, @Random DocumentContent expectedContent) {
        given(repository.download(documentId)).willReturn(expectedContent);

        DocumentContent result = sut.download(documentId);

        assertThat(result).isEqualTo(expectedContent);

        then(repository).should().download(documentId);
    }

    @Test
    void shouldPropagateExceptionFromRepository(@Random String documentId) {
        RuntimeException expectedException = new RuntimeException("Download failed");

        given(repository.download(documentId)).willThrow(expectedException);

        assertThatThrownBy(() ->
                sut.download(documentId))
                .isSameAs(expectedException);

        then(repository).should().download(documentId);
    }
}
