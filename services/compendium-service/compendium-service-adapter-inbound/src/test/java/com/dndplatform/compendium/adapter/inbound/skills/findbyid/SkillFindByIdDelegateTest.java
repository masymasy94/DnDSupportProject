package com.dndplatform.compendium.adapter.inbound.skills.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.skills.findall.mapper.SkillViewModelMapper;
import com.dndplatform.compendium.domain.SkillFindByIdService;
import com.dndplatform.compendium.domain.model.Skill;
import com.dndplatform.compendium.view.model.vm.SkillViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class SkillFindByIdDelegateTest {

    @Mock
    private SkillFindByIdService service;

    @Mock
    private SkillViewModelMapper mapper;

    private SkillFindByIdDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new SkillFindByIdDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToServiceAndMapper(@Random Skill skill, @Random SkillViewModel expected) {
        given(service.findById(skill.id())).willReturn(skill);
        given(mapper.apply(skill)).willReturn(expected);

        SkillViewModel result = sut.findById(skill.id());

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).findById(skill.id());
        then(mapper).should(inOrder).apply(skill);
        inOrder.verifyNoMoreInteractions();
    }
}
