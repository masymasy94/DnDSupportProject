package com.dndplatform.campaign.adapter.inbound.delete;

import com.dndplatform.campaign.domain.CampaignDeleteService;
import com.dndplatform.campaign.view.model.CampaignDeleteResource;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class CampaignDeleteDelegate implements CampaignDeleteResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignDeleteService service;

    @Inject
    public CampaignDeleteDelegate(CampaignDeleteService service) {
        this.service = service;
    }

    @Override
    public void delete(Long id, Long userId) {
        log.info(() -> "Deleting campaign %d by user %d".formatted(id, userId));
        service.delete(id, userId);
    }
}
