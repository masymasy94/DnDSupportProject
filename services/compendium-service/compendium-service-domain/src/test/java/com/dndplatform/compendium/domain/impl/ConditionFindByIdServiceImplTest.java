package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.compendium.domain.ConditionFindByIdService;
import com.dndplatform.compendium.domain.model.Condition;
import com.dndplatform.compendium.domain.repository.ConditionFindByIdRepository;
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
class ConditionFindByIdServiceImplTest {

    @Mock
    private ConditionFindByIdRepository repository;

    private ConditionFindByIdService sut;

    @BeforeEach
    void setUp() {
        sut = new ConditionFindByIdServiceImpl(repository);
    }

    @Test
    void shouldFindConditionById() {
        short id = 1;
        Condition expected = new Condition(id, "Blinded");

        given(repository.findById(id)).willReturn(Optional.of(expected));

        Condition result = sut.findById(id);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findById(id);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenConditionDoesNotExist() {
        int id = 999;

        given(repository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.findById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Condition not found with id: " + id);

        then(repository).should().findById(id);
    }
}
