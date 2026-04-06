package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.CampaignSummary;
import com.dndplatform.campaign.domain.model.PagedResult;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class CampaignFindByUserRepositoryJpaTest {

    @Mock
    private CampaignPanacheRepository panacheRepository;
    @Mock
    private CampaignEntityMapper mapper;

    private CampaignFindByUserRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignFindByUserRepositoryJpa(panacheRepository, mapper);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturnPagedResultWithMappedSummaries() {
        Long userId = 1L;
        CampaignEntity entity = new CampaignEntity();
        CampaignSummary summary = mock(CampaignSummary.class);

        PanacheQuery<CampaignEntity> mockQuery = mock(PanacheQuery.class);
        given(panacheRepository.findByMemberUserId(userId)).willReturn(mockQuery);
        given(mockQuery.count()).willReturn(1L);
        given(mockQuery.page(any())).willReturn(mockQuery);
        given(mockQuery.list()).willReturn(List.of(entity));
        given(mapper.toCampaignSummary(entity)).willReturn(summary);

        PagedResult result = sut.findByUserId(userId, 0, 10);

        assertThat(result.content()).containsExactly(summary);
        assertThat(result.totalElements()).isEqualTo(1L);
        assertThat(result.page()).isEqualTo(0);
        assertThat(result.size()).isEqualTo(10);
        then(panacheRepository).should().findByMemberUserId(userId);
        then(mapper).should().toCampaignSummary(entity);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturnEmptyPagedResultWhenNoCampaignsFound() {
        Long userId = 2L;

        PanacheQuery<CampaignEntity> mockQuery = mock(PanacheQuery.class);
        given(panacheRepository.findByMemberUserId(userId)).willReturn(mockQuery);
        given(mockQuery.count()).willReturn(0L);
        given(mockQuery.page(any())).willReturn(mockQuery);
        given(mockQuery.list()).willReturn(List.of());

        PagedResult result = sut.findByUserId(userId, 0, 10);

        assertThat(result.content()).isEmpty();
        assertThat(result.totalElements()).isEqualTo(0L);
        then(mapper).shouldHaveNoInteractions();
    }
}
