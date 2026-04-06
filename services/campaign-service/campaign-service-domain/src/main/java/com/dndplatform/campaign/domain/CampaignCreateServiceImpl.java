package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignCreate;
import com.dndplatform.campaign.domain.model.MemberRole;
import com.dndplatform.campaign.domain.repository.CampaignCreateRepository;
import com.dndplatform.campaign.domain.repository.CampaignMemberAddRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class CampaignCreateServiceImpl implements CampaignCreateService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final CampaignCreateRepository campaignRepository;
    private final CampaignMemberAddRepository memberRepository;

    @Inject
    public CampaignCreateServiceImpl(CampaignCreateRepository campaignRepository,
                                     CampaignMemberAddRepository memberRepository) {
        this.campaignRepository = campaignRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public Campaign create(CampaignCreate input) {
        log.info(() -> "Creating campaign: %s".formatted(input.name()));

        Campaign campaign = campaignRepository.save(input);

        memberRepository.add(campaign.id(), input.dungeonMasterId(), null, MemberRole.DUNGEON_MASTER);

        log.info(() -> "Campaign created with ID: %d".formatted(campaign.id()));
        return campaign;
    }
}
