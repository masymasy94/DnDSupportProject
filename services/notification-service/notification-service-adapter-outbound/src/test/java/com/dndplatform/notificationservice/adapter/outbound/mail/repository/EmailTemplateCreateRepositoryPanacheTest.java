package com.dndplatform.notificationservice.adapter.outbound.mail.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.notificationservice.adapter.outbound.mail.entity.EmailTemplateEntity;
import com.dndplatform.notificationservice.domain.model.EmailTemplate;
import com.dndplatform.notificationservice.domain.model.EmailTemplateResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class EmailTemplateCreateRepositoryPanacheTest {

    @Mock
    private EmailTemplatePanacheRepository panacheRepository;

    @Mock
    private EmailTemplateToEntityMapper toEntityMapper;

    @Mock
    private EmailTemplateEntityToResultMapper toResultMapper;

    private EmailTemplateCreateRepositoryPanache sut;

    @BeforeEach
    void setUp() {
        sut = new EmailTemplateCreateRepositoryPanache(panacheRepository, toEntityMapper, toResultMapper);
    }

    @Test
    void shouldSaveAndReturnResult(@Random EmailTemplate template,
                                   @Random EmailTemplateEntity entity,
                                   @Random EmailTemplateResult expected) {
        given(toEntityMapper.apply(template)).willReturn(entity);
        willDoNothing().given(panacheRepository).persist(entity);
        given(toResultMapper.apply(entity)).willReturn(expected);

        var result = sut.save(template);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(toEntityMapper, panacheRepository, toResultMapper);
        then(toEntityMapper).should(inOrder).apply(template);
        then(panacheRepository).should(inOrder).persist(entity);
        then(toResultMapper).should(inOrder).apply(entity);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldCallMappersAndPersistInOrder(@Random EmailTemplate template,
                                             @Random EmailTemplateEntity entity,
                                             @Random EmailTemplateResult expected) {
        given(toEntityMapper.apply(template)).willReturn(entity);
        willDoNothing().given(panacheRepository).persist(entity);
        given(toResultMapper.apply(entity)).willReturn(expected);

        sut.save(template);

        var inOrder = inOrder(toEntityMapper, panacheRepository, toResultMapper);
        then(toEntityMapper).should(inOrder).apply(template);
        then(panacheRepository).should(inOrder).persist(entity);
        then(toResultMapper).should(inOrder).apply(entity);
    }
}
