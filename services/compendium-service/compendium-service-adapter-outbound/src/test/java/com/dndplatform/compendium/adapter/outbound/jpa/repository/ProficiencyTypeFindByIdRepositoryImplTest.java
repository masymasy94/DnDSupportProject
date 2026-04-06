package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.ProficiencyTypeEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.ProficiencyTypeMapper;
import com.dndplatform.compendium.domain.model.ProficiencyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ProficiencyTypeFindByIdRepositoryImplTest {

    @Mock private ProficiencyTypePanacheRepository panacheRepository;
    @Mock private ProficiencyTypeMapper mapper;
    private ProficiencyTypeFindByIdRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ProficiencyTypeFindByIdRepositoryImpl(panacheRepository, mapper);
    }

    @Test
    void shouldReturnMappedProficiencyType(@Random ProficiencyTypeEntity entity, @Random ProficiencyType expected) {
        given(panacheRepository.findByIdOptional(1L)).willReturn(Optional.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        var result = sut.findById(1);

        assertThat(result).contains(expected);
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        given(panacheRepository.findByIdOptional(999L)).willReturn(Optional.empty());

        var result = sut.findById(999);

        assertThat(result).isEmpty();
    }
}
