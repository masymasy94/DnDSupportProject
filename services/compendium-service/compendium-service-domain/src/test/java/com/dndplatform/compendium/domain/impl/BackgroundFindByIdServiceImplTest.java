package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.BackgroundFindByIdService;
import com.dndplatform.compendium.domain.model.Background;
import com.dndplatform.compendium.domain.repository.BackgroundFindByIdRepository;
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
class BackgroundFindByIdServiceImplTest {

    @Mock
    private BackgroundFindByIdRepository repository;

    private BackgroundFindByIdService sut;

    @BeforeEach
    void setUp() {
        sut = new BackgroundFindByIdServiceImpl(repository);
    }

    @Test
    void shouldReturnBackgroundWhenBackgroundExists(@Random Background expected) {
        given(repository.findById(expected.id())).willReturn(Optional.of(expected));

        Background result = sut.findById(expected.id());

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findById(expected.id());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenBackgroundDoesNotExist() {
        int id = 999;
        given(repository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.findById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Background not found with id: " + id);

        then(repository).should().findById(id);
    }
}
