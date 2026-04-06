package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.compendium.domain.ProficiencyTypeFindByIdService;
import com.dndplatform.compendium.domain.model.ProficiencyType;
import com.dndplatform.compendium.domain.repository.ProficiencyTypeFindByIdRepository;
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
class ProficiencyTypeFindByIdServiceImplTest {

    @Mock
    private ProficiencyTypeFindByIdRepository repository;

    private ProficiencyTypeFindByIdService sut;

    @BeforeEach
    void setUp() {
        sut = new ProficiencyTypeFindByIdServiceImpl(repository);
    }

    @Test
    void shouldFindProficiencyTypeById() {
        int id = 1;
        ProficiencyType expected = new ProficiencyType((short) id, "Armor");

        given(repository.findById(id)).willReturn(Optional.of(expected));

        ProficiencyType result = sut.findById(id);

        assertThat(result).isEqualTo(expected);
        then(repository).should().findById(id);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenProficiencyTypeDoesNotExist() {
        int id = 999;

        given(repository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.findById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Proficiency type not found with id: " + id);

        then(repository).should().findById(id);
    }
}
