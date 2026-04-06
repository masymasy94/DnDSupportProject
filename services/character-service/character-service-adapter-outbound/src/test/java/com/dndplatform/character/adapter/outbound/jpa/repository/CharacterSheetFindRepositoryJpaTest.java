package com.dndplatform.character.adapter.outbound.jpa.repository;

import com.dndplatform.character.adapter.outbound.jpa.entity.CharacterSheetEntity;
import com.dndplatform.character.domain.model.CharacterSheetData;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CharacterSheetFindRepositoryJpaTest {

    @Mock
    private CharacterSheetPanacheRepository panacheRepository;

    private CharacterSheetFindRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterSheetFindRepositoryJpa(panacheRepository);
    }

    @Test
    void findByCharacterId_shouldReturnMappedData_whenEntityExists() {
        Long characterId = 5L;
        CharacterSheetEntity entity = new CharacterSheetEntity();
        entity.fileName = "sheet.pdf";
        entity.contentType = "application/pdf";
        entity.fileSize = 1024L;
        entity.pdfData = new byte[]{1, 2, 3};

        given(panacheRepository.findByCharacterId(characterId)).willReturn(Optional.of(entity));

        Optional<CharacterSheetData> result = sut.findByCharacterId(characterId);

        assertThat(result).isPresent();
        assertThat(result.get().fileName()).isEqualTo("sheet.pdf");
        assertThat(result.get().contentType()).isEqualTo("application/pdf");
        assertThat(result.get().fileSize()).isEqualTo(1024L);

        then(panacheRepository).should().findByCharacterId(characterId);
    }

    @Test
    void findByCharacterId_shouldReturnEmpty_whenEntityNotFound() {
        Long characterId = 99L;
        given(panacheRepository.findByCharacterId(characterId)).willReturn(Optional.empty());

        Optional<CharacterSheetData> result = sut.findByCharacterId(characterId);

        assertThat(result).isEmpty();
    }
}
