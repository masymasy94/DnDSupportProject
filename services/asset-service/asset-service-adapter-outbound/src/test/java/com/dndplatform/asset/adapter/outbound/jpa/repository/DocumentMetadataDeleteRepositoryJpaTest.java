package com.dndplatform.asset.adapter.outbound.jpa.repository;

import com.dndplatform.asset.adapter.outbound.jpa.entity.DocumentMetadataEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class DocumentMetadataDeleteRepositoryJpaTest {

    @Mock
    private DocumentMetadataPanacheRepository panacheRepository;

    private DocumentMetadataDeleteRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentMetadataDeleteRepositoryJpa(panacheRepository);
    }

    @Test
    void shouldDeleteEntityWhenFound() {
        var entity = new DocumentMetadataEntity();
        given(panacheRepository.findById("doc-1")).willReturn(entity);

        sut.delete("doc-1");

        then(panacheRepository).should().findById("doc-1");
        then(panacheRepository).should().delete(entity);
    }

    @Test
    void shouldDoNothingWhenEntityNotFound() {
        given(panacheRepository.findById("doc-404")).willReturn(null);

        sut.delete("doc-404");

        then(panacheRepository).should().findById("doc-404");
        then(panacheRepository).shouldHaveNoMoreInteractions();
    }
}
