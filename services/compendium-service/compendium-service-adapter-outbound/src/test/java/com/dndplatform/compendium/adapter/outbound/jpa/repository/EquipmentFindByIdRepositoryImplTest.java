package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.EquipmentEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.EquipmentMapper;
import com.dndplatform.compendium.domain.model.Equipment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class EquipmentFindByIdRepositoryImplTest {

    @Mock
    private EquipmentPanacheRepository panacheRepository;

    @Mock
    private EquipmentMapper mapper;

    private EquipmentFindByIdRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new EquipmentFindByIdRepositoryImpl(panacheRepository, mapper);
    }

    @Test
    void shouldReturnMappedEquipment(@Random EquipmentEntity entity, @Random Equipment expected) {
        given(panacheRepository.findByIdOptional(1L)).willReturn(Optional.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        assertThat(sut.findById(1)).contains(expected);
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        given(panacheRepository.findByIdOptional(999L)).willReturn(Optional.empty());

        assertThat(sut.findById(999)).isEmpty();
    }
}
