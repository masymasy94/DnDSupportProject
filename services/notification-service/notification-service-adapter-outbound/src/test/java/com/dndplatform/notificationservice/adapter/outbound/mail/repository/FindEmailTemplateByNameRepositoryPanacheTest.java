package com.dndplatform.notificationservice.adapter.outbound.mail.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.notificationservice.adapter.outbound.mail.entity.EmailTemplateEntity;
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
class FindEmailTemplateByNameRepositoryPanacheTest {

    @Mock
    private EmailTemplatePanacheRepository panacheRepository;

    private FindEmailTemplateByNameRepositoryPanache sut;

    @BeforeEach
    void setUp() {
        sut = new FindEmailTemplateByNameRepositoryPanache(panacheRepository);
    }

    @Test
    void shouldReturnMappedTemplateWhenFound(@Random String name) {
        var entity = new EmailTemplateEntity();
        entity.id = 42L;
        entity.setName(name);
        entity.setSubject("A subject");
        entity.setHtmlContent("<p>Content</p>");

        given(panacheRepository.findActiveByName(name)).willReturn(Optional.of(entity));

        var result = sut.findByName(name);

        assertThat(result).isPresent();
        var details = result.get();
        assertThat(details.id()).isEqualTo(42L);
        assertThat(details.name()).isEqualTo(name);
        assertThat(details.subject()).isEqualTo("A subject");
        assertThat(details.htmlContent()).isEqualTo("<p>Content</p>");

        then(panacheRepository).should().findActiveByName(name);
        then(panacheRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void shouldReturnEmptyWhenNotFound(@Random String name) {
        given(panacheRepository.findActiveByName(name)).willReturn(Optional.empty());

        var result = sut.findByName(name);

        assertThat(result).isEmpty();
        then(panacheRepository).should().findActiveByName(name);
    }
}
