package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.compendium.domain.ToolTypeFindByIdService;
import com.dndplatform.compendium.domain.model.ToolType;
import com.dndplatform.compendium.domain.repository.ToolTypeFindByIdRepository;
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

@ExtendWith(MockitoExtension.class)
class ToolTypeFindByIdServiceImplTest {

    @Mock
    private ToolTypeFindByIdRepository repository;

    private ToolTypeFindByIdService sut;

    @BeforeEach
    void setUp() {
        sut = new ToolTypeFindByIdServiceImpl(repository);
    }

    @Test
    void shouldFindToolTypeById() {
        int id = 2;
        ToolType expected = new ToolType((short) id, "Thieves' Tools", "Artisan's Tools");

        given(repository.findById(id)).willReturn(Optional.of(expected));

        ToolType result = sut.findById(id);

        assertThat(result).isEqualTo(expected);
        then(repository).should().findById(id);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenToolTypeDoesNotExist() {
        int id = 999;

        given(repository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.findById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Tool type not found with id: " + id);

        then(repository).should().findById(id);
    }
}
