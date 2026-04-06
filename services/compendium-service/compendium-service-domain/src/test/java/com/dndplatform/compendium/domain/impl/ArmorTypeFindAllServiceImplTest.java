package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.ArmorTypeFindAllService;
import com.dndplatform.compendium.domain.model.ArmorType;
import com.dndplatform.compendium.domain.repository.ArmorTypeFindAllRepository;
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
class ArmorTypeFindAllServiceImplTest {

    @Mock
    private ArmorTypeFindAllRepository repository;

    private ArmorTypeFindAllService sut;

    @BeforeEach
    void setUp() {
        sut = new ArmorTypeFindAllServiceImpl(repository);
    }

    @Test
    void shouldFindAll(@Random ArmorType armorType) {
        List<ArmorType> expected = List.of(armorType);
        given(repository.findAllArmorTypes()).willReturn(expected);

        List<ArmorType> result = sut.findAll();

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findAllArmorTypes();
        inOrder.verifyNoMoreInteractions();
    }
}
