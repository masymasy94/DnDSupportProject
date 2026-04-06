package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.compendium.domain.model.MagicItem;
import com.dndplatform.compendium.domain.model.SourceType;
import com.dndplatform.compendium.domain.repository.MagicItemFindByIdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith(MockitoExtension.class)
class MagicItemFindByIdServiceImplTest {

    @Mock
    private MagicItemFindByIdRepository repository;

    private MagicItemFindByIdServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new MagicItemFindByIdServiceImpl(repository);
    }

    @Test
    void shouldFindMagicItemById() {
        int id = 1;
        MagicItem expected = new MagicItem(
                id,
                "Bag of Holding",
                "Uncommon",
                "Wondrous Item",
                false,
                null,
                "This bag has an interior space considerably larger than its outside dimensions.",
                "weighs 15 lbs, 4×2 ft",
                SourceType.OFFICIAL,
                null,
                null,
                true
        );

        given(repository.findById(id)).willReturn(Optional.of(expected));

        MagicItem result = service.findById(id);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findById(id);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenMagicItemDoesNotExist() {
        int id = 999;

        given(repository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Magic item not found with id: " + id);

        then(repository).should().findById(id);
    }
}
