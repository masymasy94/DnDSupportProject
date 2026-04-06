package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.repository.CampaignNoteFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class CampaignNoteFindByIdRepositoryJpa implements CampaignNoteFindByIdRepository {

    private final CampaignNotePanacheRepository panacheRepository;
    private final CampaignEntityMapper mapper;

    @Inject
    public CampaignNoteFindByIdRepositoryJpa(CampaignNotePanacheRepository panacheRepository,
                                             CampaignEntityMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<CampaignNote> findById(Long noteId) {
        return panacheRepository.findByIdOptional(noteId).map(mapper::toCampaignNote);
    }
}
