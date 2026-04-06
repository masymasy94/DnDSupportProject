package com.dndplatform.compendium.adapter.inbound.proficiencytypes.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.proficiencytypes.findall.mapper.ProficiencyTypeViewModelMapper;
import com.dndplatform.compendium.domain.ProficiencyTypeFindAllService;
import com.dndplatform.compendium.domain.model.ProficiencyType;
import com.dndplatform.compendium.view.model.vm.ProficiencyTypeViewModel;
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
class ProficiencyTypeFindAllDelegateTest {

    @Mock
    private ProficiencyTypeFindAllService service;

    @Mock
    private ProficiencyTypeViewModelMapper mapper;

    private ProficiencyTypeFindAllDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new ProficiencyTypeFindAllDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToService(@Random ProficiencyType p1, @Random ProficiencyType p2,
                                 @Random ProficiencyTypeViewModel vm1, @Random ProficiencyTypeViewModel vm2) {
        given(service.findAll()).willReturn(List.of(p1, p2));
        given(mapper.apply(p1)).willReturn(vm1);
        given(mapper.apply(p2)).willReturn(vm2);

        List<ProficiencyTypeViewModel> result = sut.findAll();

        assertThat(result).containsExactly(vm1, vm2);
        then(service).should().findAll();
        then(mapper).should().apply(p1);
        then(mapper).should().apply(p2);
    }
}
