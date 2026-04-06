package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.SpeciesFindByIdService;
import com.dndplatform.compendium.domain.model.Species;
import com.dndplatform.compendium.domain.repository.SpeciesFindByIdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class SpeciesFindByIdServiceImplTest {

    @Mock
    private SpeciesFindByIdRepository repository;

    private SpeciesFindByIdService sut;

    @BeforeEach
    void setUp() {
        sut = new SpeciesFindByIdServiceImpl(repository);
    }

    @Test
    void shouldReturnSpeciesWhenSpeciesExists(@Random Species expected) {
        given(repository.findById(expected.id())).willReturn(Optional.of(expected));

        Optional<Species> result = sut.findById(expected.id());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findById(expected.id());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldReturnEmptyOptionalWhenSpeciesDoesNotExist() {
        int id = 999;
        given(repository.findById(id)).willReturn(Optional.empty());

        Optional<Species> result = sut.findById(id);

        assertThat(result).isEmpty();

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findById(id);
        inOrder.verifyNoMoreInteractions();
    }
}
