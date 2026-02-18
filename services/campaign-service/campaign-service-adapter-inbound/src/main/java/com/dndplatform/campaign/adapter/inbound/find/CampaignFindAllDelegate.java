package com.dndplatform.campaign.adapter.inbound.find;

import com.dndplatform.campaign.adapter.inbound.find.mapper.CampaignSummaryViewModelMapper;
import com.dndplatform.campaign.domain.CampaignFindByUserService;
import com.dndplatform.campaign.domain.model.PagedResult;
import com.dndplatform.campaign.view.model.CampaignFindAllResource;
import com.dndplatform.campaign.view.model.vm.PagedCampaignsViewModel;
import com.dndplatform.campaign.view.model.vm.PagedCampaignsViewModelBuilder;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class CampaignFindAllDelegate implements CampaignFindAllResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignFindByUserService service;
    private final CampaignSummaryViewModelMapper summaryMapper;

    @Inject
    public CampaignFindAllDelegate(CampaignFindByUserService service,
                                   CampaignSummaryViewModelMapper summaryMapper) {
        this.service = service;
        this.summaryMapper = summaryMapper;
    }

    @Override
    public PagedCampaignsViewModel findAll(Long userId, int page, int size) {
        log.info(() -> "Finding campaigns for user %d - page: %d, size: %d".formatted(userId, page, size));

        PagedResult result = service.findByUserId(userId, page, size);

        var content = result.content().stream()
                .map(summaryMapper)
                .toList();

        return PagedCampaignsViewModelBuilder.builder()
                .withContent(content)
                .withPage(result.page())
                .withSize(result.size())
                .withTotalElements(result.totalElements())
                .withTotalPages(result.totalPages())
                .build();
    }
}
