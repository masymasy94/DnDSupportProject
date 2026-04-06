package com.dndplatform.compendium.adapter.inbound.classes.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.classes.findall.mapper.CharacterClassViewModelMapper;
import com.dndplatform.compendium.domain.CharacterClassFindAllService;
import com.dndplatform.compendium.domain.model.CharacterClass;
import com.dndplatform.compendium.view.model.vm.CharacterClassViewModel;
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
class CharacterClassFindAllDelegateTest {

    @Mock
    private CharacterClassFindAllService service;

    @Mock
    private CharacterClassViewModelMapper mapper;

    private CharacterClassFindAllDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterClassFindAllDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToService(@Random CharacterClass c1, @Random CharacterClass c2,
                                 @Random CharacterClassViewModel vm1, @Random CharacterClassViewModel vm2) {
        given(service.findAll()).willReturn(List.of(c1, c2));
        given(mapper.apply(c1)).willReturn(vm1);
        given(mapper.apply(c2)).willReturn(vm2);

        List<CharacterClassViewModel> result = sut.findAll();

        assertThat(result).containsExactly(vm1, vm2);
        then(service).should().findAll();
        then(mapper).should().apply(c1);
        then(mapper).should().apply(c2);
    }
}
