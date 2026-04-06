package com.dndplatform.asset.adapter.inbound.documents.uploadbatch;

import com.dndplatform.asset.view.model.DocumentUploadBatchResource;
import com.dndplatform.asset.view.model.vm.DocumentViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class DocumentUploadBatchResourceImplTest {

    @Mock
    private DocumentUploadBatchResource delegate;

    @Mock
    private FileUpload file1;

    @Mock
    private FileUpload file2;

    private DocumentUploadBatchResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentUploadBatchResourceImpl(delegate);
    }

    @Test
    void shouldDelegateUploadMultiple(@Random DocumentViewModel doc1,
                                      @Random DocumentViewModel doc2) {
        String uploadedBy = "user-1";
        List<FileUpload> files = List.of(file1, file2);
        List<DocumentViewModel> expected = List.of(doc1, doc2);
        given(delegate.uploadMultiple(files, uploadedBy)).willReturn(expected);

        List<DocumentViewModel> result = sut.uploadMultiple(files, uploadedBy);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).uploadMultiple(files, uploadedBy);
        inOrder.verifyNoMoreInteractions();
    }
}
