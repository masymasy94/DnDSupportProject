package com.dndplatform.notificationservice.adapter.outbound.mail.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.notificationservice.adapter.outbound.mail.entity.EmailTemplateEntity;
import com.dndplatform.notificationservice.domain.model.EmailTemplateDetails;
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
class FindEmailTemplateByIdRepositoryPanacheTest {

    @Mock
    private EmailTemplatePanacheRepository panacheRepository;

    private FindEmailTemplateByIdRepositoryPanache sut;

    @BeforeEach
    void setUp() {
        sut = new FindEmailTemplateByIdRepositoryPanache(panacheRepository);
    }

    @Test
    void shouldReturnMappedTemplateWhenFound(@Random Long id) {
        var entity = new EmailTemplateEntity();
        entity.id = id;
        entity.setName("welcome");
        entity.setSubject("Welcome!");
        entity.setHtmlContent("<p>Hello</p>");

        given(panacheRepository.findByIdOptional(id)).willReturn(Optional.of(entity));

        var result = sut.findById(id);

        assertThat(result).isPresent();
        var details = result.get();
        assertThat(details.id()).isEqualTo(id);
        assertThat(details.name()).isEqualTo("welcome");
        assertThat(details.subject()).isEqualTo("Welcome!");
        assertThat(details.htmlContent()).isEqualTo("<p>Hello</p>");

        then(panacheRepository).should().findByIdOptional(id);
        then(panacheRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void shouldReturnEmptyWhenNotFound(@Random Long id) {
        given(panacheRepository.findByIdOptional(id)).willReturn(Optional.empty());

        var result = sut.findById(id);

        assertThat(result).isEmpty();
        then(panacheRepository).should().findByIdOptional(id);
    }
}
