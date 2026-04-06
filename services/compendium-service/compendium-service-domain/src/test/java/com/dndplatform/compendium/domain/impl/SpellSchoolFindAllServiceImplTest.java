package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.SpellSchoolFindAllService;
import com.dndplatform.compendium.domain.model.SpellSchool;
import com.dndplatform.compendium.domain.repository.SpellSchoolFindAllRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class SpellSchoolFindAllServiceImplTest {

    @Mock
    private SpellSchoolFindAllRepository repository;

    private SpellSchoolFindAllService sut;

    @BeforeEach
    void setUp() {
        sut = new SpellSchoolFindAllServiceImpl(repository);
    }

    @Test
    void shouldFindAll(@Random SpellSchool spellSchool) {
        List<SpellSchool> expected = List.of(spellSchool);
        given(repository.findAllSpellSchools()).willReturn(expected);

        List<SpellSchool> result = sut.findAll();

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findAllSpellSchools();
        inOrder.verifyNoMoreInteractions();
    }
}
