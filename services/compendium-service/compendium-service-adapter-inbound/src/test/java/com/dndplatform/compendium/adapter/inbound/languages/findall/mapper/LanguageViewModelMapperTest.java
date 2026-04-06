package com.dndplatform.compendium.adapter.inbound.languages.findall.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.model.Language;
import com.dndplatform.compendium.view.model.vm.LanguageViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class LanguageViewModelMapperTest {

    private final LanguageViewModelMapper sut = Mappers.getMapper(LanguageViewModelMapper.class);

    @Test
    void shouldMapToViewModel(@Random Language language) {
        LanguageViewModel result = sut.apply(language);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(language.id());
        assertThat(result.name()).isEqualTo(language.name());
        assertThat(result.script()).isEqualTo(language.script());
        assertThat(result.type()).isEqualTo(language.type());
    }
}
