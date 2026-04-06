package com.dndplatform.character.adapter.outbound.jpa.repository;

import com.dndplatform.character.adapter.outbound.jpa.entity.CharacterEntity;
import com.dndplatform.character.adapter.outbound.jpa.mapper.CharacterSummaryMapper;
import com.dndplatform.character.domain.model.CharacterSummary;
import com.dndplatform.character.domain.model.PagedResult;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CharacterFindAllRepositoryJpaTest {

    @Mock
    private CharacterSummaryMapper mapper;

    @Mock
    private CharacterPanacheRepository panacheRepository;

    private CharacterFindAllRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterFindAllRepositoryJpa(mapper, panacheRepository);
    }

    @Test
    void findAll_shouldReturnPagedResultWithMappedSummaries() {
        int page = 0;
        int size = 5;
        CharacterEntity entity = new CharacterEntity();
        CharacterSummary summary = new CharacterSummary(1L, "Frodo", "Hobbit", "Rogue", 3, 20, 20, 12);

        given(panacheRepository.countAll()).willReturn(1L);
        given(panacheRepository.findAllPaged(page, size)).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(summary);

        PagedResult result = sut.findAll(page, size);

        assertThat(result.content()).containsExactly(summary);
        assertThat(result.totalElements()).isEqualTo(1L);
        assertThat(result.totalPages()).isEqualTo(1);
        assertThat(result.page()).isEqualTo(page);
        assertThat(result.size()).isEqualTo(size);

        then(panacheRepository).should().countAll();
        then(panacheRepository).should().findAllPaged(page, size);
    }

    @Test
    void findAll_shouldReturnEmptyResult_whenNoCharacters() {
        int page = 0;
        int size = 10;

        given(panacheRepository.countAll()).willReturn(0L);
        given(panacheRepository.findAllPaged(page, size)).willReturn(List.of());

        PagedResult result = sut.findAll(page, size);

        assertThat(result.content()).isEmpty();
        assertThat(result.totalElements()).isEqualTo(0L);
        assertThat(result.totalPages()).isEqualTo(0);
    }
}
