package integration.document;

import com.dndplatform.asset.adapter.outbound.jpa.entity.DocumentMetadataEntity;
import com.dndplatform.test.entity.TestEntityProvider;

import java.util.List;

public class DocumentMetadataEntityProvider implements TestEntityProvider {

    public static final String ID = "test-document-id";
    public static final String FILE_NAME = "test-document.pdf";
    public static final String CONTENT_TYPE = "application/pdf";
    public static final Long SIZE = 1024L;
    public static final String UPLOADED_BY = "1";
    public static final String RAG_STATUS = "COMPLETED";

    @Override
    public List<Object> provideEntities() {
        return List.of(getEntity());
    }

    public static DocumentMetadataEntity getEntity() {
        var entity = new DocumentMetadataEntity();
        entity.id = ID;
        entity.fileName = FILE_NAME;
        entity.contentType = CONTENT_TYPE;
        entity.size = SIZE;
        entity.uploadedBy = UPLOADED_BY;
        entity.ragStatus = RAG_STATUS;
        return entity;
    }
}
