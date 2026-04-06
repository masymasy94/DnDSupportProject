package com.dndplatform.asset.adapter.inbound.documents.list;

import com.dndplatform.asset.domain.DocumentListService;
import com.dndplatform.asset.domain.model.DocumentListItem;
import com.dndplatform.asset.domain.model.RagStatus;
import com.dndplatform.asset.view.model.vm.DocumentListItemViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class DocumentListDelegateTest {

    @Mock
    private DocumentListService service;

    private DocumentListDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentListDelegate(service);
    }

    @Test
    void shouldReturnMappedDocumentList() {
        var item1 = new DocumentListItem("id-1", "file1.pdf", RagStatus.COMPLETED);
        var item2 = new DocumentListItem("id-2", "file2.pdf", RagStatus.PENDING);
        given(service.listAll()).willReturn(List.of(item1, item2));

        List<DocumentListItemViewModel> result = sut.listAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo("id-1");
        assertThat(result.get(0).fileName()).isEqualTo("file1.pdf");
        assertThat(result.get(0).ragStatus()).isEqualTo("COMPLETED");
        assertThat(result.get(1).id()).isEqualTo("id-2");
        assertThat(result.get(1).ragStatus()).isEqualTo("PENDING");

        var inOrder = inOrder(service);
        then(service).should(inOrder).listAll();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldReturnEmptyListWhenNoDocuments() {
        given(service.listAll()).willReturn(List.of());

        List<DocumentListItemViewModel> result = sut.listAll();

        assertThat(result).isEmpty();

        var inOrder = inOrder(service);
        then(service).should(inOrder).listAll();
        inOrder.verifyNoMoreInteractions();
    }
}
