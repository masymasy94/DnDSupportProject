package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.LanguageFindAllService;
import com.dndplatform.compendium.domain.model.Language;
import com.dndplatform.compendium.domain.repository.LanguageFindAllRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class LanguageFindAllServiceImplTest {

    @Mock
    private LanguageFindAllRepository repository;

    private LanguageFindAllService sut;

    @BeforeEach
    void setUp() {
        sut = new LanguageFindAllServiceImpl(repository);
    }

    @Test
    void shouldFindAll(@Random Language language) {
        List<Language> expected = List.of(language);
        given(repository.findAllLanguages()).willReturn(expected);

        List<Language> result = sut.findAll();

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findAllLanguages();
        inOrder.verifyNoMoreInteractions();
    }
}
