package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.CampaignSummary;
import com.dndplatform.campaign.domain.model.PagedResult;
import com.dndplatform.campaign.domain.model.PagedResultBuilder;
import com.dndplatform.campaign.domain.repository.CampaignFindByUserRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CampaignFindByUserRepositoryJpa implements CampaignFindByUserRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignEntityMapper mapper;

    @Inject
    public CampaignFindByUserRepositoryJpa(CampaignEntityMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public PagedResult findByUserId(Long userId, int page, int size) {
        log.info(() -> "Finding campaigns for user %d - page: %d, size: %d".formatted(userId, page, size));

        var query = CampaignEntity.find(
                "id IN (SELECT m.campaign.id FROM CampaignMemberEntity m WHERE m.userId = ?1)",
                userId
        );

        long totalElements = query.count();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        List<CampaignSummary> content = query
                .page(Page.of(page, size))
                .<CampaignEntity>list()
                .stream()
                .map(mapper::toCampaignSummary)
                .toList();

        return PagedResultBuilder.builder()
                .withContent(content)
                .withPage(page)
                .withSize(size)
                .withTotalElements(totalElements)
                .withTotalPages(totalPages)
                .build();
    }
}
