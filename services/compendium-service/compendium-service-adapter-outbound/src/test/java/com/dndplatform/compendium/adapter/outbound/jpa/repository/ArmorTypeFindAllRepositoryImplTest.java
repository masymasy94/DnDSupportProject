package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.ArmorTypeEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.ArmorTypeMapper;
import com.dndplatform.compendium.domain.model.ArmorType;
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
class ArmorTypeFindAllRepositoryImplTest {

    @Mock
    private ArmorTypePanacheRepository panacheRepository;

    @Mock
    private ArmorTypeMapper mapper;

    private ArmorTypeFindAllRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ArmorTypeFindAllRepositoryImpl(panacheRepository, mapper);
    }

    @Test
    void shouldReturnMappedArmorType(@Random ArmorTypeEntity entity, @Random ArmorType expected) {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        var result = sut.findAllArmorTypes();

        assertThat(result).containsExactly(expected);
    }

    @Test
    void shouldReturnEmptyListWhenNoArmorTypeFound() {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of());

        var result = sut.findAllArmorTypes();

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
