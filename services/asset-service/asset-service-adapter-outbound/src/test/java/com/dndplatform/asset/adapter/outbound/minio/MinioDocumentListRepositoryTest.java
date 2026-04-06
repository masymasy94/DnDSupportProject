package com.dndplatform.asset.adapter.outbound.minio;

import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith(MockitoExtension.class)
class MinioDocumentListRepositoryTest {

    @Mock
    private MinioClient minioClient;

    private MinioDocumentListRepository sut;

    @BeforeEach
    void setUp() {
        sut = new MinioDocumentListRepository(minioClient, "test-bucket");
    }

    @Test
    void shouldReturnEmptyListWhenNoObjects() {
        given(minioClient.listObjects(any())).willReturn(List.of());

        var result = sut.listAll();

        assertThat(result).isEmpty();
    }

    @Test
    void shouldThrowRuntimeExceptionWhenMinioFails() {
        willThrow(new RuntimeException("MinIO unavailable")).given(minioClient).listObjects(any());

        assertThatThrownBy(() -> sut.listAll())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to list documents");
    }
}
