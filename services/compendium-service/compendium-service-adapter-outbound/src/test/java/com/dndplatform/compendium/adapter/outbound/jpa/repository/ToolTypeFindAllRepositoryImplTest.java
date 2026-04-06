package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.ToolTypeEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.ToolTypeMapper;
import com.dndplatform.compendium.domain.model.ToolType;
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
class ToolTypeFindAllRepositoryImplTest {

    @Mock private ToolTypePanacheRepository panacheRepository;
    @Mock private ToolTypeMapper mapper;
    private ToolTypeFindAllRepositoryImpl sut;

    @BeforeEach void setUp() { sut = new ToolTypeFindAllRepositoryImpl(panacheRepository, mapper); }

    @Test
    void shouldReturnAllToolTypesWithoutFilter(@Random ToolTypeEntity entity, @Random ToolType expected) {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        var result = sut.findAllToolTypes(null);

        assertThat(result).containsExactly(expected);
    }

    @Test
    void shouldReturnAllToolTypesWhenCategoryIsBlank(@Random ToolTypeEntity entity, @Random ToolType expected) {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        var result = sut.findAllToolTypes("   ");

        assertThat(result).containsExactly(expected);
    }

    @Test
    void shouldReturnFilteredToolTypesByCategory(@Random ToolTypeEntity entity, @Random ToolType expected) {
        String category = "Artisan's Tools";
        given(panacheRepository.findByCategory(category)).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        var result = sut.findAllToolTypes(category);

        assertThat(result).containsExactly(expected);
    }

    @Test
    void shouldReturnEmptyListWhenNoCategoryAndNoResults() {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of());

        var result = sut.findAllToolTypes(null);

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
