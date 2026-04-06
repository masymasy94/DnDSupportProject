package com.dndplatform.character.adapter.inbound.sheet;

import com.dndplatform.character.domain.model.CharacterSheetData;
import com.dndplatform.character.domain.repository.CharacterSheetFindRepository;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CharacterSheetDownloadDelegateTest {

    @Mock
    private CharacterSheetFindRepository sheetRepository;

    private CharacterSheetDownloadDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterSheetDownloadDelegate(sheetRepository);
    }

    @Test
    void shouldReturnPdfResponseWhenSheetExists(@Random Long characterId) {
        var pdfBytes = new byte[]{1, 2, 3, 4, 5};
        var sheetData = new CharacterSheetData("gandalf.pdf", "application/pdf", pdfBytes.length, pdfBytes);

        given(sheetRepository.findByCharacterId(characterId)).willReturn(Optional.of(sheetData));

        Response result = sut.downloadSheet(characterId);

        assertThat(result.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(result.getEntity()).isEqualTo(pdfBytes);
        assertThat(result.getHeaderString("Content-Disposition"))
                .contains("attachment; filename=\"gandalf.pdf\"");

        var inOrder = inOrder(sheetRepository);
        then(sheetRepository).should(inOrder).findByCharacterId(characterId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenSheetDoesNotExist(@Random Long characterId) {
        given(sheetRepository.findByCharacterId(characterId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.downloadSheet(characterId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Character sheet not found for character ID: " + characterId);

        var inOrder = inOrder(sheetRepository);
        then(sheetRepository).should(inOrder).findByCharacterId(characterId);
        inOrder.verifyNoMoreInteractions();
    }
}
