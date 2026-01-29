package com.dndplatform.asset.adapter.outbound.minio;

import com.dndplatform.asset.domain.model.DocumentListItem;
import com.dndplatform.asset.domain.repository.DocumentListRepository;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class MinioDocumentListRepository implements DocumentListRepository {

    private final java.util.logging.Logger log = Logger.getLogger(getClass().getName());

    private final MinioClient minioClient;
    private final String bucketName;

    @Inject
    public MinioDocumentListRepository(MinioClient minioClient,
                                       @ConfigProperty(name = "minio.bucket") String bucketName) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
    }

    @Override
    public List<DocumentListItem> listAll() {
        List<DocumentListItem> documents = new ArrayList<>();
        try {
            var objects = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .recursive(true)
                            .build()
            );

            for (var result : objects) {
                String objectName = result.get().objectName();
                int slashIndex = objectName.indexOf('/');
                if (slashIndex > 0) {
                    String documentId = objectName.substring(0, slashIndex);
                    String fileName = objectName.substring(slashIndex + 1);
                    documents.add(new DocumentListItem(documentId, fileName));
                }
            }
        } catch (Exception e) {
            log.severe(() -> "Error listing all documents %s".formatted(e.getMessage()));
            throw new RuntimeException("Failed to list documents", e);
        }
        return documents;
    }
}
