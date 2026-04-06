package com.dndplatform.character.adapter.inbound.update;

import com.dndplatform.character.view.model.vm.CharacterViewModel;
import com.dndplatform.character.view.model.vm.UpdateCharacterRequest;
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
class CharacterUpdateResourceImplTest {

    @Mock
    private CharacterUpdateDelegate delegate;

    private CharacterUpdateResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterUpdateResourceImpl(delegate);
    }

    @Test
    void shouldDelegateUpdate(@Random Long id, @Random UpdateCharacterRequest request, @Random CharacterViewModel expected) {
        given(delegate.update(id, request)).willReturn(expected);

        var result = sut.update(id, request);

        assertThat(result).isEqualTo(expected);
        then(delegate).should().update(id, request);
        then(delegate).shouldHaveNoMoreInteractions();
    }
}
