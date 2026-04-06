package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.DamageTypeFindAllService;
import com.dndplatform.compendium.domain.model.DamageType;
import com.dndplatform.compendium.domain.repository.DamageTypeFindAllRepository;
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
class DamageTypeFindAllServiceImplTest {

    @Mock
    private DamageTypeFindAllRepository repository;

    private DamageTypeFindAllService sut;

    @BeforeEach
    void setUp() {
        sut = new DamageTypeFindAllServiceImpl(repository);
    }

    @Test
    void shouldFindAll(@Random DamageType damageType) {
        List<DamageType> expected = List.of(damageType);
        given(repository.findAllDamageTypes()).willReturn(expected);

        List<DamageType> result = sut.findAll();

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findAllDamageTypes();
        inOrder.verifyNoMoreInteractions();
    }
}
