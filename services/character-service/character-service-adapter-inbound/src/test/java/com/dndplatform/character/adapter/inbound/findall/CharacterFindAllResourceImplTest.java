package com.dndplatform.character.adapter.inbound.findall;

import com.dndplatform.character.view.model.CharacterFindAllResource;
import com.dndplatform.character.view.model.vm.PagedCharactersViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CharacterFindAllResourceImplTest {

    @Mock
    private CharacterFindAllResource delegate;

    private CharacterFindAllResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterFindAllResourceImpl(delegate);
    }

    @Test
    void shouldDelegateFindAll(@Random int page, @Random int size, @Random PagedCharactersViewModel expected) {
        given(delegate.findAll(page, size)).willReturn(expected);

        var result = sut.findAll(page, size);

        assertThat(result).isEqualTo(expected);
        then(delegate).should().findAll(page, size);
        then(delegate).shouldHaveNoMoreInteractions();
    }
}
