package com.dndplatform.compendium.adapter.inbound.languages.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.languages.findall.mapper.LanguageViewModelMapper;
import com.dndplatform.compendium.domain.LanguageFindByIdService;
import com.dndplatform.compendium.domain.model.Language;
import com.dndplatform.compendium.view.model.vm.LanguageViewModel;
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
class LanguageFindByIdDelegateTest {

    @Mock
    private LanguageFindByIdService service;

    @Mock
    private LanguageViewModelMapper mapper;

    private LanguageFindByIdDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new LanguageFindByIdDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToServiceAndMapper(@Random Language language, @Random LanguageViewModel expected) {
        given(service.findById(language.id())).willReturn(language);
        given(mapper.apply(language)).willReturn(expected);

        LanguageViewModel result = sut.findById(language.id());

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).findById(language.id());
        then(mapper).should(inOrder).apply(language);
        inOrder.verifyNoMoreInteractions();
    }
}
