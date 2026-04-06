package com.dndplatform.documentqa.adapter.outbound.minio;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MinioDocumentFetchRepositoryTest {

    @Mock
    private MinioClient minioClient;

    @Mock
    private GetObjectResponse response;

    private MinioDocumentFetchRepository sut;

    @BeforeEach
    void setUp() {
        sut = new MinioDocumentFetchRepository(minioClient, "test-bucket");
    }

    @Test
    void shouldReturnInputStreamFromMinio() throws Exception {
        given(minioClient.getObject(any(GetObjectArgs.class))).willReturn(response);

        var result = sut.fetch("doc-1", "file.pdf");

        assertThat(result).isSameAs(response);
    }

    @Test
    void shouldThrowRuntimeExceptionWhenMinioFails() throws Exception {
        given(minioClient.getObject(any(GetObjectArgs.class)))
                .willThrow(new RuntimeException("MinIO error"));

        assertThatThrownBy(() -> sut.fetch("doc-1", "file.pdf"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to fetch document from MinIO");
    }
}
