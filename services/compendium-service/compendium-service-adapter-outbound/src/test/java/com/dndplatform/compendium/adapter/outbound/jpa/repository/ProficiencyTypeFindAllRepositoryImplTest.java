package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.ProficiencyTypeEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.ProficiencyTypeMapper;
import com.dndplatform.compendium.domain.model.ProficiencyType;
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
class ProficiencyTypeFindAllRepositoryImplTest {

    @Mock private ProficiencyTypePanacheRepository panacheRepository;
    @Mock private ProficiencyTypeMapper mapper;
    private ProficiencyTypeFindAllRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ProficiencyTypeFindAllRepositoryImpl(panacheRepository, mapper);
    }

    @Test
    void shouldReturnMappedProficiencyType(@Random ProficiencyTypeEntity entity, @Random ProficiencyType expected) {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        assertThat(sut.findAllProficiencyTypes()).containsExactly(expected);
    }

    @Test
    void shouldReturnEmptyList() {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of());

        assertThat(sut.findAllProficiencyTypes()).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
