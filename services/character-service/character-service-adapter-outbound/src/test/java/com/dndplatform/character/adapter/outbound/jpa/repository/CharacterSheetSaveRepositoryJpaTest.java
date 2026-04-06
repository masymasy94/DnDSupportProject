package com.dndplatform.character.adapter.outbound.jpa.repository;

import com.dndplatform.character.adapter.outbound.jpa.entity.CharacterSheetEntity;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CharacterSheetSaveRepositoryJpaTest {

    @Mock
    private CharacterSheetPanacheRepository panacheRepository;

    private CharacterSheetSaveRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterSheetSaveRepositoryJpa(panacheRepository);
    }

    @Test
    void saveSheet_shouldDeleteExistingAndPersistNew() {
        Long characterId = 3L;
        String fileName = "character_sheet.pdf";
        String contentType = "application/pdf";
        byte[] pdfData = new byte[]{0x25, 0x50, 0x44, 0x46};

        given(panacheRepository.deleteByCharacterId(characterId)).willReturn(1L);
        willDoNothing().given(panacheRepository).persist(any(CharacterSheetEntity.class));

        sut.saveSheet(characterId, fileName, contentType, pdfData);

        var inOrder = inOrder(panacheRepository);
        then(panacheRepository).should(inOrder).deleteByCharacterId(characterId);

        ArgumentCaptor<CharacterSheetEntity> captor = ArgumentCaptor.forClass(CharacterSheetEntity.class);
        then(panacheRepository).should(inOrder).persist(captor.capture());

        CharacterSheetEntity saved = captor.getValue();
        assertThat(saved.characterId).isEqualTo(characterId);
        assertThat(saved.fileName).isEqualTo(fileName);
        assertThat(saved.contentType).isEqualTo(contentType);
        assertThat(saved.fileSize).isEqualTo(pdfData.length);
        assertThat(saved.pdfData).isEqualTo(pdfData);
    }
}
