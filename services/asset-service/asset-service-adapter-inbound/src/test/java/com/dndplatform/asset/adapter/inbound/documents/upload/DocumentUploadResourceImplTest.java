package com.dndplatform.asset.adapter.inbound.documents.upload;

import com.dndplatform.asset.view.model.DocumentUploadResource;
import com.dndplatform.asset.view.model.vm.DocumentViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class DocumentUploadResourceImplTest {

    @Mock
    private DocumentUploadResource delegate;

    @Mock
    private FileUpload file;

    private DocumentUploadResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentUploadResourceImpl(delegate);
    }

    @Test
    void shouldDelegateUpload(@Random DocumentViewModel expected) {
        String uploadedBy = "user-1";
        given(delegate.upload(file, uploadedBy)).willReturn(expected);

        DocumentViewModel result = sut.upload(file, uploadedBy);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).upload(file, uploadedBy);
        inOrder.verifyNoMoreInteractions();
    }
}
