package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.AlignmentEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.AlignmentMapper;
import com.dndplatform.compendium.domain.model.Alignment;
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
class AlignmentFindAllRepositoryImplTest {

    @Mock
    private AlignmentPanacheRepository panacheRepository;

    @Mock
    private AlignmentMapper mapper;

    private AlignmentFindAllRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new AlignmentFindAllRepositoryImpl(panacheRepository, mapper);
    }

    @Test
    void shouldReturnMappedAlignment(@Random AlignmentEntity entity, @Random Alignment expected) {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        var result = sut.findAllAlignment();

        assertThat(result).containsExactly(expected);
    }

    @Test
    void shouldReturnEmptyListWhenNoAlignmentFound() {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of());

        var result = sut.findAllAlignment();

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
