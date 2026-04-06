package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.WeaponTypeFindAllService;
import com.dndplatform.compendium.domain.model.WeaponType;
import com.dndplatform.compendium.domain.repository.WeaponTypeFindAllRepository;
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
class WeaponTypeFindAllServiceImplTest {

    @Mock
    private WeaponTypeFindAllRepository repository;

    private WeaponTypeFindAllService sut;

    @BeforeEach
    void setUp() {
        sut = new WeaponTypeFindAllServiceImpl(repository);
    }

    @Test
    void shouldFindAll(@Random WeaponType weaponType) {
        String category = "MARTIAL";
        List<WeaponType> expected = List.of(weaponType);
        given(repository.findAllWeaponTypes(category)).willReturn(expected);

        List<WeaponType> result = sut.findAll(category);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findAllWeaponTypes(category);
        inOrder.verifyNoMoreInteractions();
    }
}
