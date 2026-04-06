package com.dndplatform.character.adapter.inbound.importsheet;

import com.dndplatform.character.adapter.inbound.create.mapper.CharacterViewModelMapper;
import com.dndplatform.character.adapter.inbound.importsheet.mapper.PdfFieldToCharacterMapper;
import com.dndplatform.character.adapter.inbound.importsheet.parser.PdfCharacterSheetParser;
import com.dndplatform.character.domain.CharacterCreateService;
import com.dndplatform.character.domain.model.Character;
import com.dndplatform.character.domain.model.CharacterCreate;
import com.dndplatform.character.domain.repository.CharacterSheetSaveRepository;
import com.dndplatform.character.view.model.vm.CharacterViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CharacterImportSheetDelegateTest {

    @Mock
    private PdfCharacterSheetParser parser;

    @Mock
    private PdfFieldToCharacterMapper fieldMapper;

    @Mock
    private CharacterCreateService createService;

    @Mock
    private CharacterSheetSaveRepository sheetRepository;

    @Mock
    private CharacterViewModelMapper viewModelMapper;

    private CharacterImportSheetDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterImportSheetDelegate(parser, fieldMapper, createService, sheetRepository, viewModelMapper);
    }

    @Test
    void shouldImportSheetWithUserIdAndReturnViewModel(
            @Random Character character,
            @Random CharacterCreate characterCreate,
            @Random CharacterViewModel viewModel
    ) {
        var pdfBytes = new byte[]{1, 2, 3};
        var fileName = "gandalf.pdf";
        var contentType = "application/pdf";
        var userId = 42L;
        var fields = Map.of("CharacterName", "Gandalf");

        given(parser.extractFormFields(pdfBytes)).willReturn(fields);
        given(fieldMapper.mapToCharacterCreate(fields, userId)).willReturn(characterCreate);
        given(createService.create(characterCreate)).willReturn(character);
        given(viewModelMapper.apply(character)).willReturn(viewModel);

        var result = sut.importSheetWithUserId(pdfBytes, fileName, contentType, userId);

        assertThat(result).isEqualTo(viewModel);

        var inOrder = inOrder(parser, fieldMapper, createService, sheetRepository, viewModelMapper);
        then(parser).should(inOrder).extractFormFields(pdfBytes);
        then(fieldMapper).should(inOrder).mapToCharacterCreate(fields, userId);
        then(createService).should(inOrder).create(characterCreate);
        then(sheetRepository).should(inOrder).saveSheet(character.id(), fileName, contentType, pdfBytes);
        then(viewModelMapper).should(inOrder).apply(character);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenParserFails(@Random Character character) {
        var pdfBytes = new byte[]{1, 2, 3};
        var fileName = "bad.pdf";
        var contentType = "application/pdf";
        var userId = 1L;

        willThrow(new RuntimeException("Failed to parse PDF")).given(parser).extractFormFields(pdfBytes);

        assertThatThrownBy(() -> sut.importSheetWithUserId(pdfBytes, fileName, contentType, userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Failed to parse PDF");

        then(fieldMapper).shouldHaveNoInteractions();
        then(createService).shouldHaveNoInteractions();
        then(sheetRepository).shouldHaveNoInteractions();
        then(viewModelMapper).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowWhenCreateServiceFails(
            @Random CharacterCreate characterCreate
    ) {
        var pdfBytes = new byte[]{1, 2, 3};
        var fileName = "gandalf.pdf";
        var contentType = "application/pdf";
        var userId = 42L;
        var fields = Map.of("CharacterName", "Gandalf");

        given(parser.extractFormFields(pdfBytes)).willReturn(fields);
        given(fieldMapper.mapToCharacterCreate(fields, userId)).willReturn(characterCreate);
        given(createService.create(characterCreate)).willThrow(new RuntimeException("Validation failed"));

        assertThatThrownBy(() -> sut.importSheetWithUserId(pdfBytes, fileName, contentType, userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Validation failed");

        then(sheetRepository).shouldHaveNoInteractions();
        then(viewModelMapper).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowUnsupportedOperationForImportSheet() {
        assertThatThrownBy(() -> sut.importSheet(null, "file.pdf"))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Use importSheetWithUserId instead");
    }
}
