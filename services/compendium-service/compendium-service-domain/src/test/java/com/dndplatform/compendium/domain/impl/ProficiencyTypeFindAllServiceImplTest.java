package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.ProficiencyTypeFindAllService;
import com.dndplatform.compendium.domain.model.ProficiencyType;
import com.dndplatform.compendium.domain.repository.ProficiencyTypeFindAllRepository;
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
class ProficiencyTypeFindAllServiceImplTest {

    @Mock
    private ProficiencyTypeFindAllRepository repository;

    private ProficiencyTypeFindAllService sut;

    @BeforeEach
    void setUp() {
        sut = new ProficiencyTypeFindAllServiceImpl(repository);
    }

    @Test
    void shouldFindAll(@Random ProficiencyType proficiencyType) {
        List<ProficiencyType> expected = List.of(proficiencyType);
        given(repository.findAllProficiencyTypes()).willReturn(expected);

        List<ProficiencyType> result = sut.findAll();

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findAllProficiencyTypes();
        inOrder.verifyNoMoreInteractions();
    }
}
