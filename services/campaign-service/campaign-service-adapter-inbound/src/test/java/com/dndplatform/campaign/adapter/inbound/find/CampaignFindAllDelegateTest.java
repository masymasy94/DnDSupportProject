package com.dndplatform.campaign.adapter.inbound.find;

import com.dndplatform.campaign.adapter.inbound.find.mapper.CampaignSummaryViewModelMapper;
import com.dndplatform.campaign.domain.CampaignFindByUserService;
import com.dndplatform.campaign.domain.model.CampaignStatus;
import com.dndplatform.campaign.domain.model.CampaignSummary;
import com.dndplatform.campaign.domain.model.PagedResult;
import com.dndplatform.campaign.view.model.vm.CampaignSummaryViewModel;
import com.dndplatform.campaign.view.model.vm.PagedCampaignsViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CampaignFindAllDelegateTest {

    @Mock
    private CampaignFindByUserService service;

    @Mock
    private CampaignSummaryViewModelMapper summaryMapper;

    private CampaignFindAllDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignFindAllDelegate(service, summaryMapper);
    }

    @Test
    void shouldReturnPagedCampaigns(
            @Random CampaignSummaryViewModel vm1,
            @Random CampaignSummaryViewModel vm2) {

        Long userId = 1L;
        int page = 0;
        int size = 10;

        CampaignSummary summary1 = new CampaignSummary(1L, "Campaign One", CampaignStatus.ACTIVE, 3);
        CampaignSummary summary2 = new CampaignSummary(2L, "Campaign Two", CampaignStatus.DRAFT, 5);
        PagedResult pagedResult = new PagedResult(List.of(summary1, summary2), page, size, 2L, 1);

        given(service.findByUserId(userId, page, size)).willReturn(pagedResult);
        given(summaryMapper.apply(summary1)).willReturn(vm1);
        given(summaryMapper.apply(summary2)).willReturn(vm2);

        PagedCampaignsViewModel result = sut.findAll(userId, page, size);

        assertThat(result.content()).containsExactly(vm1, vm2);
        assertThat(result.page()).isEqualTo(page);
        assertThat(result.size()).isEqualTo(size);
        assertThat(result.totalElements()).isEqualTo(2L);
        assertThat(result.totalPages()).isEqualTo(1);

        var inOrder = inOrder(service, summaryMapper);
        then(service).should(inOrder).findByUserId(userId, page, size);
        then(summaryMapper).should(inOrder).apply(summary1);
        then(summaryMapper).should(inOrder).apply(summary2);
        inOrder.verifyNoMoreInteractions();
    }
}
