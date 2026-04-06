package com.dndplatform.asset.domain.impl;

import com.dndplatform.asset.domain.model.DocumentListItem;
import com.dndplatform.asset.domain.model.RagStatus;
import com.dndplatform.asset.domain.repository.DocumentListRepository;
import com.dndplatform.asset.domain.repository.DocumentMetadataFindRepository;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class DocumentListServiceImplTest {

    @Mock
    private DocumentListRepository repository;

    @Mock
    private DocumentMetadataFindRepository metadataRepository;

    private DocumentListServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentListServiceImpl(repository, metadataRepository);
    }

    @Test
    void shouldListAllDocumentsWithRagStatus(@Random DocumentListItem item1, @Random DocumentListItem item2) {
        List<DocumentListItem> documentItems = List.of(
                new DocumentListItem(item1.id(), item1.fileName()),
                new DocumentListItem(item2.id(), item2.fileName())
        );

        given(repository.listAll()).willReturn(documentItems);
        given(metadataRepository.findRagStatus(item1.id())).willReturn(Optional.of(RagStatus.COMPLETED));
        given(metadataRepository.findRagStatus(item2.id())).willReturn(Optional.of(RagStatus.PROCESSING));

        List<DocumentListItem> result = sut.listAll();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(
                new DocumentListItem(item1.id(), item1.fileName(), RagStatus.COMPLETED),
                new DocumentListItem(item2.id(), item2.fileName(), RagStatus.PROCESSING)
        );

        var inOrder = inOrder(repository, metadataRepository);
        then(repository).should(inOrder).listAll();
        then(metadataRepository).should(inOrder).findRagStatus(item1.id());
        then(metadataRepository).should(inOrder).findRagStatus(item2.id());
    }

    @Test
    void shouldReturnEmptyListWhenNoDocumentsExist() {
        given(repository.listAll()).willReturn(List.of());

        List<DocumentListItem> result = sut.listAll();

        assertThat(result).isEmpty();

        then(repository).should().listAll();
        then(metadataRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldReturnNullRagStatusWhenNotFound(@Random DocumentListItem item1) {
        List<DocumentListItem> documentItems = List.of(
                new DocumentListItem(item1.id(), item1.fileName())
        );

        given(repository.listAll()).willReturn(documentItems);
        given(metadataRepository.findRagStatus(item1.id())).willReturn(Optional.empty());

        List<DocumentListItem> result = sut.listAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).ragStatus()).isNull();

        then(repository).should().listAll();
        then(metadataRepository).should().findRagStatus(item1.id());
    }
}
