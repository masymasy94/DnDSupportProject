package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.CharacterClassFindAllService;
import com.dndplatform.compendium.domain.model.CharacterClass;
import com.dndplatform.compendium.domain.repository.CharacterClassFindAllRepository;
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
class CharacterClassFindAllServiceImplTest {

    @Mock
    private CharacterClassFindAllRepository repository;

    private CharacterClassFindAllService sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterClassFindAllServiceImpl(repository);
    }

    @Test
    void shouldFindAll(@Random CharacterClass characterClass) {
        List<CharacterClass> expected = List.of(characterClass);
        given(repository.findAllClasses()).willReturn(expected);

        List<CharacterClass> result = sut.findAll();

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findAllClasses();
        inOrder.verifyNoMoreInteractions();
    }
}
