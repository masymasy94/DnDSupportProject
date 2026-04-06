package com.dndplatform.compendium.adapter.inbound.spellschools.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.spellschools.findall.mapper.SpellSchoolViewModelMapper;
import com.dndplatform.compendium.domain.SpellSchoolFindAllService;
import com.dndplatform.compendium.domain.model.SpellSchool;
import com.dndplatform.compendium.view.model.vm.SpellSchoolViewModel;
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
class SpellSchoolFindAllDelegateTest {

    @Mock
    private SpellSchoolFindAllService service;

    @Mock
    private SpellSchoolViewModelMapper mapper;

    private SpellSchoolFindAllDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new SpellSchoolFindAllDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToService(@Random SpellSchool ss1, @Random SpellSchool ss2,
                                 @Random SpellSchoolViewModel vm1, @Random SpellSchoolViewModel vm2) {
        given(service.findAll()).willReturn(List.of(ss1, ss2));
        given(mapper.apply(ss1)).willReturn(vm1);
        given(mapper.apply(ss2)).willReturn(vm2);

        List<SpellSchoolViewModel> result = sut.findAll();

        assertThat(result).containsExactly(vm1, vm2);
        then(service).should().findAll();
        then(mapper).should().apply(ss1);
        then(mapper).should().apply(ss2);
    }
}
