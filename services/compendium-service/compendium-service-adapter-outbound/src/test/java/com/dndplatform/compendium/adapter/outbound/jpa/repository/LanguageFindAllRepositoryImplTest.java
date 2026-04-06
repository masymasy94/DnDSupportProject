package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.LanguageEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.LanguageMapper;
import com.dndplatform.compendium.domain.model.Language;
import io.quarkus.panache.common.Sort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class LanguageFindAllRepositoryImplTest {

    @Mock
    private LanguagePanacheRepository panacheRepository;

    @Mock
    private LanguageMapper mapper;

    private LanguageFindAllRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new LanguageFindAllRepositoryImpl(panacheRepository, mapper);
    }

    @Test
    void shouldReturnMappedLanguages(@Random LanguageEntity entity, @Random Language expected) {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        assertThat(sut.findAllLanguages()).containsExactly(expected);
    }

    @Test
    void shouldReturnEmptyList() {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of());

        assertThat(sut.findAllLanguages()).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
