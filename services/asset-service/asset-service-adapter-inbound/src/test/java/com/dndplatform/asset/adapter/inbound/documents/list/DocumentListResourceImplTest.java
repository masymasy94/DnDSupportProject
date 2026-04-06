package com.dndplatform.asset.adapter.inbound.documents.list;

import com.dndplatform.asset.view.model.DocumentListResource;
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
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class DocumentListResourceImplTest {

    @Mock
    private DocumentListResource delegate;

    private DocumentListResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentListResourceImpl(delegate);
    }

    @Test
    void shouldDelegateListAll(@Random DocumentListItemViewModel item1,
                               @Random DocumentListItemViewModel item2) {
        List<DocumentListItemViewModel> expected = List.of(item1, item2);
        given(delegate.listAll()).willReturn(expected);

        List<DocumentListItemViewModel> result = sut.listAll();

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).listAll();
        inOrder.verifyNoMoreInteractions();
    }
}
