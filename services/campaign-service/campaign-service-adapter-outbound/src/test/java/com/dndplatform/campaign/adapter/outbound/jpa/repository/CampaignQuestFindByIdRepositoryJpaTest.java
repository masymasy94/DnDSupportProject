package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignQuestEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
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
class CampaignQuestFindByIdRepositoryJpaTest {

    @Mock
    private CampaignQuestPanacheRepository panacheRepository;
    @Mock
    private CampaignEntityMapper mapper;

    private CampaignQuestFindByIdRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignQuestFindByIdRepositoryJpa(panacheRepository, mapper);
    }

    @Test
    void shouldReturnMappedQuestWhenFound(@Random CampaignQuest expected) {
        Long questId = 1L;
        CampaignQuestEntity entity = new CampaignQuestEntity();
        given(panacheRepository.findByIdOptional(questId)).willReturn(Optional.of(entity));
        given(mapper.toCampaignQuest(entity)).willReturn(expected);

        Optional<CampaignQuest> result = sut.findById(questId);

        assertThat(result).isPresent().contains(expected);
        then(panacheRepository).should().findByIdOptional(questId);
        then(mapper).should().toCampaignQuest(entity);
    }

    @Test
    void shouldReturnEmptyWhenQuestNotFound() {
        Long questId = 99L;
        given(panacheRepository.findByIdOptional(questId)).willReturn(Optional.empty());

        Optional<CampaignQuest> result = sut.findById(questId);

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
