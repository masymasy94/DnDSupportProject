package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.WeaponTypeEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.WeaponTypeMapper;
import com.dndplatform.compendium.domain.model.WeaponType;
import io.quarkus.panache.common.Sort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class WeaponTypeFindAllRepositoryImplTest {

    @Mock private WeaponTypePanacheRepository panacheRepository;
    @Mock private WeaponTypeMapper mapper;
    private WeaponTypeFindAllRepositoryImpl sut;

    @BeforeEach void setUp() { sut = new WeaponTypeFindAllRepositoryImpl(panacheRepository, mapper); }

    @Test
    void shouldReturnAllWeaponTypesWithoutFilter(@Random WeaponTypeEntity entity, @Random WeaponType expected) {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        var result = sut.findAllWeaponTypes(null);

        assertThat(result).containsExactly(expected);
    }

    @Test
    void shouldReturnAllWeaponTypesWhenCategoryIsBlank(@Random WeaponTypeEntity entity, @Random WeaponType expected) {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        var result = sut.findAllWeaponTypes("   ");

        assertThat(result).containsExactly(expected);
    }

    @Test
    void shouldReturnFilteredWeaponTypesByCategory(@Random WeaponTypeEntity entity, @Random WeaponType expected) {
        String category = "Martial Weapons";
        given(panacheRepository.findByCategory(category)).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        var result = sut.findAllWeaponTypes(category);

        assertThat(result).containsExactly(expected);
    }

    @Test
    void shouldReturnEmptyListWhenNoCategoryAndNoResults() {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of());

        var result = sut.findAllWeaponTypes(null);

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
