package com.dndplatform.asset.adapter.outbound.minio;

import com.dndplatform.asset.domain.model.Document;
import com.dndplatform.asset.domain.repository.DocumentUploadRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jspecify.annotations.NonNull;

import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;
import java.util.logging.Logger;

@ApplicationScoped
public class MinioDocumentUploadRepository implements DocumentUploadRepository {

    private final java.util.logging.Logger log = Logger.getLogger(getClass().getName());

    private final MinioClient minioClient;
    private final String bucketName;

    @Inject
    public MinioDocumentUploadRepository(MinioClient minioClient,
                                         @ConfigProperty(name = "minio.bucket") String bucketName) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
    }

    @Override
    public Document upload(String fileName, String contentType, InputStream inputStream, long size, String uploadedBy) {
        String documentId = UUID.randomUUID().toString();
        String objectName = documentId + "/" + fileName;

        try {
            minioClient.putObject(getObjectArgs(contentType, inputStream, size, objectName));

            log.info(() -> "Uploaded document: %s (size: %d bytes)".formatted(objectName, size));
            return getDocument(fileName, contentType, size, uploadedBy, documentId);

        } catch (Exception e) {
            log.severe(() -> "Error uploading document: " + e.getMessage());
            throw new RuntimeException("Failed to upload document", e);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    private PutObjectArgs getObjectArgs(String contentType, InputStream inputStream, long size, String objectName) {
        return PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(inputStream, size, -1)
                .contentType(contentType)
                .build();
    }

    private static @NonNull Document getDocument(String fileName, String contentType, long size, String uploadedBy, String documentId) {
        return new Document(
                documentId,
                fileName,
                contentType,
                size,
                uploadedBy,
                Instant.now()
        );
    }
}
