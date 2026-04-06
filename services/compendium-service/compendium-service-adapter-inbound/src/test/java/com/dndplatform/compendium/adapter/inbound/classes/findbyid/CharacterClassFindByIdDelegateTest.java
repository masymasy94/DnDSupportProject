package com.dndplatform.compendium.adapter.inbound.classes.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.classes.findall.mapper.CharacterClassViewModelMapper;
import com.dndplatform.compendium.domain.CharacterClassFindByIdService;
import com.dndplatform.compendium.domain.model.CharacterClass;
import com.dndplatform.compendium.view.model.vm.CharacterClassViewModel;
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
class CharacterClassFindByIdDelegateTest {

    @Mock
    private CharacterClassFindByIdService service;

    @Mock
    private CharacterClassViewModelMapper mapper;

    private CharacterClassFindByIdDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterClassFindByIdDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToServiceAndMapper(@Random CharacterClass characterClass, @Random CharacterClassViewModel expected) {
        given(service.findById(characterClass.id())).willReturn(characterClass);
        given(mapper.apply(characterClass)).willReturn(expected);

        CharacterClassViewModel result = sut.findById(characterClass.id());

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).findById(characterClass.id());
        then(mapper).should(inOrder).apply(characterClass);
        inOrder.verifyNoMoreInteractions();
    }
}
