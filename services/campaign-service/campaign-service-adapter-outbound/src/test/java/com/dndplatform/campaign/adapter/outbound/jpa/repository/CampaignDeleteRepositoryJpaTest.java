package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.common.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class CampaignDeleteRepositoryJpaTest {

    @Mock
    private CampaignPanacheRepository panacheRepository;

    private CampaignDeleteRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignDeleteRepositoryJpa(panacheRepository);
    }

    @Test
    void shouldFindAndDeleteCampaign() {
        Long id = 42L;
        CampaignEntity entity = new CampaignEntity();
        given(panacheRepository.findById(id)).willReturn(entity);
        willDoNothing().given(panacheRepository).delete(entity);

        sut.deleteById(id);

        then(panacheRepository).should().findById(id);
        then(panacheRepository).should().delete(entity);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCampaignDoesNotExist() {
        Long id = 99L;
        given(panacheRepository.findById(id)).willReturn(null);

        assertThatThrownBy(() -> sut.deleteById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");

        then(panacheRepository).should().findById(id);
        then(panacheRepository).shouldHaveNoMoreInteractions();
    }
}
