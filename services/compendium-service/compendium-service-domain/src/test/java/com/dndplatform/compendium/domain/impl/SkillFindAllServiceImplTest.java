package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.SkillFindAllService;
import com.dndplatform.compendium.domain.model.Skill;
import com.dndplatform.compendium.domain.repository.SkillFindAllRepository;
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

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class SkillFindAllServiceImplTest {

    @Mock
    private SkillFindAllRepository repository;

    private SkillFindAllService sut;

    @BeforeEach
    void setUp() {
        sut = new SkillFindAllServiceImpl(repository);
    }

    @Test
    void shouldFindAll(@Random Skill skill) {
        List<Skill> expected = List.of(skill);
        given(repository.findAllSkills()).willReturn(expected);

        List<Skill> result = sut.findAll();

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findAllSkills();
        inOrder.verifyNoMoreInteractions();
    }
}
