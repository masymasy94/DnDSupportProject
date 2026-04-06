package com.dndplatform.compendium.adapter.inbound.languages.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.languages.findall.mapper.LanguageViewModelMapper;
import com.dndplatform.compendium.domain.LanguageFindAllService;
import com.dndplatform.compendium.domain.model.Language;
import com.dndplatform.compendium.view.model.vm.LanguageViewModel;
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
class LanguageFindAllDelegateTest {

    @Mock
    private LanguageFindAllService service;

    @Mock
    private LanguageViewModelMapper mapper;

    private LanguageFindAllDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new LanguageFindAllDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToService(@Random Language l1, @Random Language l2,
                                 @Random LanguageViewModel vm1, @Random LanguageViewModel vm2) {
        given(service.findAll()).willReturn(List.of(l1, l2));
        given(mapper.apply(l1)).willReturn(vm1);
        given(mapper.apply(l2)).willReturn(vm2);

        List<LanguageViewModel> result = sut.findAll();

        assertThat(result).containsExactly(vm1, vm2);
        then(service).should().findAll();
        then(mapper).should().apply(l1);
        then(mapper).should().apply(l2);
    }
}
