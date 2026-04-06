package com.dndplatform.documentqa.adapter.outbound.minio;

import com.dndplatform.documentqa.domain.repository.DocumentFetchRepository;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.InputStream;
import java.util.logging.Logger;

@ApplicationScoped
public class MinioDocumentFetchRepository implements DocumentFetchRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final MinioClient minioClient;
    private final String bucketName;

    @Inject
    public MinioDocumentFetchRepository(MinioClient minioClient,
                                        @ConfigProperty(name = "minio.bucket") String bucketName) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
    }

    @Override
    public InputStream fetch(String documentId, String fileName) {
        String objectPath = documentId + "/" + fileName;
        log.info(() -> "Fetching document from MinIO: %s/%s".formatted(bucketName, objectPath));

        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectPath)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch document from MinIO: " + objectPath, e);
        }
    }
}
