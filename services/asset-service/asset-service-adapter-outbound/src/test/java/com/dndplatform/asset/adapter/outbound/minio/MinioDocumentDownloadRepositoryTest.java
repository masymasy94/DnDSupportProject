package com.dndplatform.asset.adapter.outbound.minio;

import io.minio.MinioClient;
import com.dndplatform.common.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MinioDocumentDownloadRepositoryTest {

    @Mock
    private MinioClient minioClient;

    private MinioDocumentDownloadRepository sut;

    @BeforeEach
    void setUp() {
        sut = new MinioDocumentDownloadRepository(minioClient, "test-bucket");
    }

    @Test
    void shouldThrowNotFoundExceptionWhenObjectDoesNotExist() {
        given(minioClient.listObjects(any())).willReturn(List.of());

        assertThatThrownBy(() -> sut.download("doc-123"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Document not found");
    }

    @Test
    void shouldThrowRuntimeExceptionWhenListObjectsFails() {
        given(minioClient.listObjects(any())).willThrow(new RuntimeException("MinIO error"));

        assertThatThrownBy(() -> sut.download("doc-123"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to download document");
    }
}
