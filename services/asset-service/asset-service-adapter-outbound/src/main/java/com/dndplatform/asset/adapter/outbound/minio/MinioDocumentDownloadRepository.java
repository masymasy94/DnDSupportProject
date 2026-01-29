package com.dndplatform.asset.adapter.outbound.minio;

import com.dndplatform.asset.domain.model.DocumentContent;
import com.dndplatform.asset.domain.repository.DocumentDownloadRepository;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.ErrorResponseException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.InputStream;
import java.util.logging.Logger;

@ApplicationScoped
public class MinioDocumentDownloadRepository implements DocumentDownloadRepository {

    private final java.util.logging.Logger log = Logger.getLogger(getClass().getName());

    private final MinioClient minioClient;
    private final String bucketName;

    @Inject
    public MinioDocumentDownloadRepository(MinioClient minioClient,
                                           @ConfigProperty(name = "minio.bucket") String bucketName) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
    }

    @Override
    public DocumentContent download(String documentId) {
        try {
            String prefix = documentId + "/";

            var objects = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(prefix)
                            .maxKeys(1)
                            .build()
            );

            String objectName = null;
            for (var result : objects) {
                objectName = result.get().objectName();
                break;
            }

            if (objectName == null) {
                throw new NotFoundException("Document not found: " + documentId);
            }

            StatObjectResponse stat = minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());

            InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());

            String fileName = objectName.substring(prefix.length());

            return new DocumentContent(
                    fileName,
                    stat.contentType(),
                    stat.size(),
                    stream
            );
        } catch (NotFoundException e) {
            throw e;
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                throw new NotFoundException("Document not found: " + documentId);
            }
            log.severe(() -> "Failed to download document: %s".formatted(documentId));
            throw new RuntimeException("Failed to download document", e);
        } catch (Exception e) {
            log.severe(() -> "Failed to download document: %s".formatted(documentId));
            throw new RuntimeException("Failed to download document", e);
        }
    }


}
