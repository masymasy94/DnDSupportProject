package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.EquipmentFindByIdService;
import com.dndplatform.compendium.domain.model.Equipment;
import com.dndplatform.compendium.domain.repository.EquipmentFindByIdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class EquipmentFindByIdServiceImplTest {

    @Mock
    private EquipmentFindByIdRepository repository;

    private EquipmentFindByIdService sut;

    @BeforeEach
    void setUp() {
        sut = new EquipmentFindByIdServiceImpl(repository);
    }

    @Test
    void shouldReturnEquipmentWhenEquipmentExists(@Random Equipment expected) {
        given(repository.findById(expected.id())).willReturn(Optional.of(expected));

        Equipment result = sut.findById(expected.id());

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findById(expected.id());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenEquipmentDoesNotExist() {
        int id = 999;
        given(repository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.findById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Equipment not found with id: " + id);

        then(repository).should().findById(id);
    }
}
