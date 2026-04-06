package com.dndplatform.character.adapter.inbound.sheet;

import com.dndplatform.character.view.model.CharacterSheetDownloadResource;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CharacterSheetDownloadResourceImplTest {

    @Mock
    private CharacterSheetDownloadResource delegate;

    private CharacterSheetDownloadResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterSheetDownloadResourceImpl(delegate);
    }

    @Test
    void shouldDelegateDownloadSheet(@Random Long characterId) {
        var expected = Response.ok(new byte[]{1, 2, 3}).build();
        given(delegate.downloadSheet(characterId)).willReturn(expected);

        var result = sut.downloadSheet(characterId);

        assertThat(result).isEqualTo(expected);
        then(delegate).should().downloadSheet(characterId);
        then(delegate).shouldHaveNoMoreInteractions();
    }
}
