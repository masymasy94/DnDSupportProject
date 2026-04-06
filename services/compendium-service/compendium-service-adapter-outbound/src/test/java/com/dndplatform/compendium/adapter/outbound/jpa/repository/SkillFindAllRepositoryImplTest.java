package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.SkillEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.SkillMapper;
import com.dndplatform.compendium.domain.model.Skill;
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
class SkillFindAllRepositoryImplTest {

    @Mock
    private SkillPanacheRepository panacheRepository;

    @Mock
    private SkillMapper mapper;

    private SkillFindAllRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new SkillFindAllRepositoryImpl(panacheRepository, mapper);
    }

    @Test
    void shouldReturnMappedSkills(@Random SkillEntity entity, @Random Skill expected) {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        var result = sut.findAllSkills();

        assertThat(result).containsExactly(expected);
    }

    @Test
    void shouldReturnEmptyListWhenNoSkillsFound() {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of());

        var result = sut.findAllSkills();

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
