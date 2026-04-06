package com.dndplatform.notificationservice.adapter.outbound.mail.repository;

import com.dndplatform.common.exception.ConflictException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.notificationservice.adapter.outbound.mail.entity.EmailTemplateEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class EmailTemplateExistsByByNameRepositoryPanacheTest {

    @Mock
    private EmailTemplatePanacheRepository panacheRepository;

    private EmailTemplateExistsByByNameRepositoryPanache sut;

    @BeforeEach
    void setUp() {
        sut = new EmailTemplateExistsByByNameRepositoryPanache(panacheRepository);
    }

    @Test
    void shouldNotThrowWhenTemplateNameDoesNotExist(@Random String name) {
        given(panacheRepository.findByName(name)).willReturn(Optional.empty());

        assertThatCode(() -> sut.checkNameNotExists(name)).doesNotThrowAnyException();

        then(panacheRepository).should().findByName(name);
        then(panacheRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void shouldThrowConflictExceptionWhenTemplateNameExists(@Random String name,
                                                             @Random EmailTemplateEntity entity) {
        given(panacheRepository.findByName(name)).willReturn(Optional.of(entity));

        assertThatThrownBy(() -> sut.checkNameNotExists(name))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining(name);

        then(panacheRepository).should().findByName(name);
    }
}
