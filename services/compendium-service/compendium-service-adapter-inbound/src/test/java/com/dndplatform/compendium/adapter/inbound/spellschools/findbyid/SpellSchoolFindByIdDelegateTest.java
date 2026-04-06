package com.dndplatform.compendium.adapter.inbound.spellschools.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.spellschools.findall.mapper.SpellSchoolViewModelMapper;
import com.dndplatform.compendium.domain.SpellSchoolFindByIdService;
import com.dndplatform.compendium.domain.model.SpellSchool;
import com.dndplatform.compendium.view.model.vm.SpellSchoolViewModel;
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
class SpellSchoolFindByIdDelegateTest {

    @Mock
    private SpellSchoolFindByIdService service;

    @Mock
    private SpellSchoolViewModelMapper mapper;

    private SpellSchoolFindByIdDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new SpellSchoolFindByIdDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToServiceAndMapper(@Random SpellSchool spellSchool, @Random SpellSchoolViewModel expected) {
        given(service.findById(spellSchool.id())).willReturn(spellSchool);
        given(mapper.apply(spellSchool)).willReturn(expected);

        SpellSchoolViewModel result = sut.findById(spellSchool.id());

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).findById(spellSchool.id());
        then(mapper).should(inOrder).apply(spellSchool);
        inOrder.verifyNoMoreInteractions();
    }
}
