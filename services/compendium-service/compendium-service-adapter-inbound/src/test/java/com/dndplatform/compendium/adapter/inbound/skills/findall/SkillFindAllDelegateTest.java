package com.dndplatform.compendium.adapter.inbound.skills.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.skills.findall.mapper.SkillViewModelMapper;
import com.dndplatform.compendium.domain.SkillFindAllService;
import com.dndplatform.compendium.domain.model.Skill;
import com.dndplatform.compendium.view.model.vm.SkillViewModel;
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
class SkillFindAllDelegateTest {

    @Mock
    private SkillFindAllService service;

    @Mock
    private SkillViewModelMapper mapper;

    private SkillFindAllDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new SkillFindAllDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToService(@Random Skill s1, @Random Skill s2,
                                 @Random SkillViewModel vm1, @Random SkillViewModel vm2) {
        given(service.findAll()).willReturn(List.of(s1, s2));
        given(mapper.apply(s1)).willReturn(vm1);
        given(mapper.apply(s2)).willReturn(vm2);

        List<SkillViewModel> result = sut.findAll();

        assertThat(result).containsExactly(vm1, vm2);
        then(service).should().findAll();
        then(mapper).should().apply(s1);
        then(mapper).should().apply(s2);
    }
}
