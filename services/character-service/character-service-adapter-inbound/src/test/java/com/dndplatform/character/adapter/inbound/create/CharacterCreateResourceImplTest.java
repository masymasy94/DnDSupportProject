package com.dndplatform.character.adapter.inbound.create;

import com.dndplatform.character.view.model.vm.CharacterViewModel;
import com.dndplatform.character.view.model.vm.CreateCharacterRequest;
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
class CharacterCreateResourceImplTest {

    @Mock
    private CharacterCreateDelegate delegate;

    private CharacterCreateResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterCreateResourceImpl(delegate);
    }

    @Test
    void shouldDelegateCreate(@Random CreateCharacterRequest request, @Random CharacterViewModel expected) {
        given(delegate.create(request)).willReturn(expected);

        var result = sut.create(request);

        assertThat(result).isEqualTo(expected);
        then(delegate).should().create(request);
        then(delegate).shouldHaveNoMoreInteractions();
    }
}
