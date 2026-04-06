package com.dndplatform.compendium.domain.impl;

import com.dndplatform.compendium.domain.MonsterFindAllService;
import com.dndplatform.compendium.domain.filter.MonsterFilterCriteria;
import com.dndplatform.compendium.domain.model.Monster;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.domain.repository.MonsterFindAllRepository;
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

@ExtendWith(MockitoExtension.class)
class MonsterFindAllServiceImplTest {

    @Mock
    private MonsterFindAllRepository repository;

    private MonsterFindAllService sut;

    @BeforeEach
    void setUp() {
        sut = new MonsterFindAllServiceImpl(repository);
    }

    @Test
    void shouldFindAllMonstersWithCriteria() {
        MonsterFilterCriteria criteria = new MonsterFilterCriteria(null, null, null, null, null, 0, 20);
        List<Monster> monsters = List.of(createMonster(1), createMonster(2));
        PagedResult<Monster> expected = new PagedResult<>(monsters, 0, 20, 2);

        given(repository.findAllMonsters(criteria)).willReturn(expected);

        PagedResult<Monster> result = sut.findAll(criteria);

        assertThat(result.content()).hasSize(2);
        assertThat(result.totalElements()).isEqualTo(2);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findAllMonsters(criteria);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldReturnEmptyPageWhenNoMonsters() {
        MonsterFilterCriteria criteria = new MonsterFilterCriteria(null, null, null, null, null, 0, 20);
        PagedResult<Monster> expected = new PagedResult<>(List.of(), 0, 20, 0);

        given(repository.findAllMonsters(criteria)).willReturn(expected);

        PagedResult<Monster> result = sut.findAll(criteria);

        assertThat(result.content()).isEmpty();
        assertThat(result.totalElements()).isZero();

        then(repository).should().findAllMonsters(criteria);
    }

    private Monster createMonster(int id) {
        return new Monster(
                id, "monster-" + id, "Monster " + id, "Medium", "Beast",
                null, "neutral", 12, null, 30, "2d8", "30 ft.",
                10, 10, 10, 10, 10, 10,
                null, null, null, "Common", "1", 50, 2,
                null, null, null, null, null, null, null, null, null, null, null, false
        );
    }
}
