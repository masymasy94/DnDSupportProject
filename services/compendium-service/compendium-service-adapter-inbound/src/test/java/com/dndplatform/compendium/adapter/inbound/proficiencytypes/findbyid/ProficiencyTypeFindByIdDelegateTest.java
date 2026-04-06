package com.dndplatform.compendium.adapter.inbound.proficiencytypes.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.proficiencytypes.findall.mapper.ProficiencyTypeViewModelMapper;
import com.dndplatform.compendium.domain.ProficiencyTypeFindByIdService;
import com.dndplatform.compendium.domain.model.ProficiencyType;
import com.dndplatform.compendium.view.model.vm.ProficiencyTypeViewModel;
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
class ProficiencyTypeFindByIdDelegateTest {

    @Mock
    private ProficiencyTypeFindByIdService service;

    @Mock
    private ProficiencyTypeViewModelMapper mapper;

    private ProficiencyTypeFindByIdDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new ProficiencyTypeFindByIdDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToServiceAndMapper(@Random ProficiencyType proficiencyType, @Random ProficiencyTypeViewModel expected) {
        given(service.findById(proficiencyType.id())).willReturn(proficiencyType);
        given(mapper.apply(proficiencyType)).willReturn(expected);

        ProficiencyTypeViewModel result = sut.findById(proficiencyType.id());

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).findById(proficiencyType.id());
        then(mapper).should(inOrder).apply(proficiencyType);
        inOrder.verifyNoMoreInteractions();
    }
}
