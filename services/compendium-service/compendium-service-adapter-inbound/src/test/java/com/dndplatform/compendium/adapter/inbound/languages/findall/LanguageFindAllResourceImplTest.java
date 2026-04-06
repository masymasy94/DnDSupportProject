package com.dndplatform.compendium.adapter.inbound.languages.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.LanguageFindAllResource;
import com.dndplatform.compendium.view.model.vm.LanguageViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class LanguageFindAllResourceImplTest {
    @Mock private LanguageFindAllResource delegate;
    private LanguageFindAllResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new LanguageFindAllResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random LanguageViewModel vm1, @Random LanguageViewModel vm2) {
        List<LanguageViewModel> expected = List.of(vm1, vm2);
        given(delegate.findAll()).willReturn(expected);
        List<LanguageViewModel> result = sut.findAll();
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findAll();
        inOrder.verifyNoMoreInteractions();
    }
}
