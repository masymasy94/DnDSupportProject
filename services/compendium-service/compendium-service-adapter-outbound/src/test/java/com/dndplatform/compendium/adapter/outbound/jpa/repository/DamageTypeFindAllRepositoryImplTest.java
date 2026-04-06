package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.DamageTypeEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.DamageTypeMapper;
import com.dndplatform.compendium.domain.model.DamageType;
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
class DamageTypeFindAllRepositoryImplTest {

    @Mock private DamageTypePanacheRepository panacheRepository;
    @Mock private DamageTypeMapper mapper;
    private DamageTypeFindAllRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new DamageTypeFindAllRepositoryImpl(panacheRepository, mapper);
    }

    @Test
    void shouldReturnMappedDamageType(@Random DamageTypeEntity entity, @Random DamageType expected) {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        assertThat(sut.findAllDamageTypes()).containsExactly(expected);
    }

    @Test
    void shouldReturnEmptyList() {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of());

        assertThat(sut.findAllDamageTypes()).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
