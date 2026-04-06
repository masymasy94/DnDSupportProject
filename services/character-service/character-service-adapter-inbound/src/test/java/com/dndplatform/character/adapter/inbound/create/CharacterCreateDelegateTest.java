package com.dndplatform.character.adapter.inbound.create;

import com.dndplatform.character.adapter.inbound.create.mapper.CharacterCreateMapper;
import com.dndplatform.character.adapter.inbound.create.mapper.CharacterViewModelMapper;
import com.dndplatform.character.domain.CharacterCreateService;
import com.dndplatform.character.domain.CharacterSheetGenerator;
import com.dndplatform.character.domain.model.Character;
import com.dndplatform.character.domain.model.CharacterCreate;
import com.dndplatform.character.domain.repository.CharacterSheetSaveRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CharacterCreateDelegateTest {

    @Mock
    private CharacterCreateService service;

    @Mock
    private CharacterCreateMapper createMapper;

    @Mock
    private CharacterViewModelMapper viewModelMapper;

    @Mock
    private CharacterSheetGenerator sheetGenerator;

    @Mock
    private CharacterSheetSaveRepository sheetRepository;

    private CharacterCreateDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterCreateDelegate(service, createMapper, viewModelMapper, sheetGenerator, sheetRepository);
    }

    @Test
    void shouldCreateCharacterAndGeneratePdf(
            @Random CreateCharacterRequest request,
            @Random CharacterCreate baseCreate,
            @Random Character character,
            @Random CharacterViewModel viewModel
    ) {
        var pdfBytes = new byte[]{1, 2, 3};

        given(createMapper.apply(request)).willReturn(baseCreate);
        given(service.create(any(CharacterCreate.class))).willReturn(character);
        given(viewModelMapper.apply(character)).willReturn(viewModel);
        given(sheetGenerator.generate(character)).willReturn(pdfBytes);

        var result = sut.create(request);

        assertThat(result).isEqualTo(viewModel);

        var inOrder = inOrder(createMapper, service, sheetGenerator, sheetRepository, viewModelMapper);
        then(createMapper).should(inOrder).apply(request);
        then(service).should(inOrder).create(any(CharacterCreate.class));
        then(sheetGenerator).should(inOrder).generate(character);
        then(sheetRepository).should(inOrder).saveSheet(character.id(), character.name() + ".pdf", "application/pdf", pdfBytes);
        then(viewModelMapper).should(inOrder).apply(character);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldCreateCharacterWithoutFailingOnPdfGenerationError(
            @Random CreateCharacterRequest request,
            @Random CharacterCreate baseCreate,
            @Random Character character,
            @Random CharacterViewModel viewModel
    ) {
        given(createMapper.apply(request)).willReturn(baseCreate);
        given(service.create(any(CharacterCreate.class))).willReturn(character);
        given(viewModelMapper.apply(character)).willReturn(viewModel);
        given(sheetGenerator.generate(character)).willThrow(new RuntimeException("PDF generation failed"));

        var result = sut.create(request);

        assertThat(result).isEqualTo(viewModel);
        then(sheetRepository).shouldHaveNoInteractions();
    }
}
