package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.compendium.domain.FeatFindByIdService;
import com.dndplatform.compendium.domain.model.Feat;
import com.dndplatform.compendium.domain.model.SourceType;
import com.dndplatform.compendium.domain.repository.FeatFindByIdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FeatFindByIdServiceImplTest {

    @Mock
    private FeatFindByIdRepository repository;

    private FeatFindByIdService sut;

    @BeforeEach
    void setUp() {
        sut = new FeatFindByIdServiceImpl(repository);
    }

    @Test
    void shouldFindFeatById() {
        int id = 1;
        Feat expected = new Feat(
                id,
                "Alert",
                "You gain a +5 bonus to initiative.",
                "None",
                null,
                null,
                "+5 to initiative",
                false,
                SourceType.OFFICIAL,
                1L,
                1L,
                false
        );

        given(repository.findById(id)).willReturn(Optional.of(expected));

        Feat result = sut.findById(id);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findById(id);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenFeatDoesNotExist() {
        int id = 999;

        given(repository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.findById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Feat not found with id: " + id);

        then(repository).should().findById(id);
    }
}