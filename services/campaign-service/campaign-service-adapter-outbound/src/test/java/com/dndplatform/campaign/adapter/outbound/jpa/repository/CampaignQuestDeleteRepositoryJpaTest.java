package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignQuestEntity;
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
class CampaignQuestDeleteRepositoryJpaTest {

    @Mock
    private CampaignQuestPanacheRepository panacheRepository;

    private CampaignQuestDeleteRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignQuestDeleteRepositoryJpa(panacheRepository);
    }

    @Test
    void shouldFindAndDeleteQuest() {
        Long questId = 1L;
        CampaignQuestEntity entity = new CampaignQuestEntity();
        given(panacheRepository.findById(questId)).willReturn(entity);
        willDoNothing().given(panacheRepository).delete(entity);

        sut.deleteById(questId);

        then(panacheRepository).should().findById(questId);
        then(panacheRepository).should().delete(entity);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenQuestDoesNotExist() {
        Long questId = 99L;
        given(panacheRepository.findById(questId)).willReturn(null);

        assertThatThrownBy(() -> sut.deleteById(questId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");

        then(panacheRepository).should().findById(questId);
        then(panacheRepository).shouldHaveNoMoreInteractions();
    }
}
