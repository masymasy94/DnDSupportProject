package com.dndplatform.notificationservice.adapter.outbound.mail.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.notificationservice.adapter.outbound.mail.entity.EmailTemplateEntity;
import com.dndplatform.notificationservice.domain.model.EmailTemplateResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class FindAllEmailTemplatesRepositoryPanacheTest {

    @Mock
    private EmailTemplatePanacheRepository panacheRepository;

    @Mock
    private EmailTemplateEntityToResultMapper mapper;

    private FindAllEmailTemplatesRepositoryPanache sut;

    @BeforeEach
    void setUp() {
        sut = new FindAllEmailTemplatesRepositoryPanache(panacheRepository, mapper);
    }

    @Test
    void shouldReturnMappedResults(@Random EmailTemplateEntity e1, @Random EmailTemplateEntity e2,
                                   @Random EmailTemplateResult r1, @Random EmailTemplateResult r2) {
        var entities = List.of(e1, e2);
        given(panacheRepository.listAll()).willReturn(entities);
        given(mapper.apply(e1)).willReturn(r1);
        given(mapper.apply(e2)).willReturn(r2);

        var result = sut.findAll();

        assertThat(result).containsExactly(r1, r2);

        var inOrder = inOrder(panacheRepository, mapper);
        then(panacheRepository).should(inOrder).listAll();
        then(mapper).should(inOrder).apply(e1);
        then(mapper).should(inOrder).apply(e2);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldReturnEmptyListWhenNoTemplates() {
        given(panacheRepository.listAll()).willReturn(List.of());

        var result = sut.findAll();

        assertThat(result).isEmpty();
        then(panacheRepository).should().listAll();
        then(mapper).shouldHaveNoInteractions();
    }
}
