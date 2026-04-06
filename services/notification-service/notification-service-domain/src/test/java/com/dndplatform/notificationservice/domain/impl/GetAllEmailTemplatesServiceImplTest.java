package com.dndplatform.notificationservice.domain.impl;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.notificationservice.domain.model.EmailTemplateResult;
import com.dndplatform.notificationservice.domain.repository.FindAllEmailTemplatesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class GetAllEmailTemplatesServiceImplTest {

    @Mock
    private FindAllEmailTemplatesRepository findAllEmailTemplatesRepository;

    private GetAllEmailTemplatesServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new GetAllEmailTemplatesServiceImpl(findAllEmailTemplatesRepository);
    }

    @Test
    void shouldReturnAllTemplates(@Random EmailTemplateResult t1, @Random EmailTemplateResult t2) {
        var templates = List.of(t1, t2);
        given(findAllEmailTemplatesRepository.findAll()).willReturn(templates);

        var result = sut.getAll();

        assertThat(result).isEqualTo(templates);
        then(findAllEmailTemplatesRepository).should(times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoTemplates() {
        given(findAllEmailTemplatesRepository.findAll()).willReturn(List.of());

        var result = sut.getAll();

        assertThat(result).isEmpty();
        then(findAllEmailTemplatesRepository).should(times(1)).findAll();
    }
}