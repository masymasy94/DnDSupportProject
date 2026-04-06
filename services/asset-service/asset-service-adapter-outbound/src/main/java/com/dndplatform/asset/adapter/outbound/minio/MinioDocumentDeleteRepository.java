package com.dndplatform.asset.adapter.outbound.minio;

import com.dndplatform.asset.domain.repository.DocumentDeleteRepository;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import com.dndplatform.common.exception.NotFoundException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class MinioDocumentDeleteRepository implements DocumentDeleteRepository {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final MinioClient minioClient;
    private final String bucketName;

    @Inject
    public MinioDocumentDeleteRepository(MinioClient minioClient,
                                         @ConfigProperty(name = "minio.bucket") String bucketName) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
    }

    @Override
    public void delete(String documentId) {
        try {
            String prefix = documentId + "/";

            var objects = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(prefix)
                            .build()
            );

            List<String> objectNames = new ArrayList<>();
            for (var result : objects) {
                objectNames.add(result.get().objectName());
            }

            if (objectNames.isEmpty()) {
                throw new NotFoundException("Document not found: " + documentId);
            }

            for (String objectName : objectNames) {
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .build()
                );
            }
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.severe(() -> "Failed to delete document from storage: %s".formatted(documentId));
            throw new RuntimeException("Failed to delete document from storage", e);
        }
    }
}
