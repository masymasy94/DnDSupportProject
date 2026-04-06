package com.dndplatform.asset.adapter.outbound.minio;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith(MockitoExtension.class)
class MinioDocumentUploadRepositoryTest {

    @Mock
    private MinioClient minioClient;

    private MinioDocumentUploadRepository sut;

    @BeforeEach
    void setUp() {
        sut = new MinioDocumentUploadRepository(minioClient, "test-bucket");
    }

    @Test
    void shouldUploadAndReturnDocument() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("content".getBytes());

        var result = sut.upload("file.pdf", "application/pdf", inputStream, 7L, "user1");

        assertThat(result.id()).isNotBlank();
        assertThat(result.fileName()).isEqualTo("file.pdf");
        assertThat(result.contentType()).isEqualTo("application/pdf");
        assertThat(result.size()).isEqualTo(7L);
        assertThat(result.uploadedBy()).isEqualTo("user1");
        then(minioClient).should().putObject(any(PutObjectArgs.class));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenMinioFails() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("content".getBytes());
        willThrow(new RuntimeException("MinIO error")).given(minioClient).putObject(any(PutObjectArgs.class));

        assertThatThrownBy(() -> sut.upload("file.pdf", "application/pdf", inputStream, 7L, "user1"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to upload document");
    }
}
