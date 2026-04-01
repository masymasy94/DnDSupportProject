package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignNoteEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.repository.CampaignNoteFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class CampaignNoteFindByIdRepositoryJpa implements CampaignNoteFindByIdRepository {

    private final CampaignEntityMapper mapper;

    @Inject
    public CampaignNoteFindByIdRepositoryJpa(CampaignEntityMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<CampaignNote> findById(Long noteId) {
        CampaignNoteEntity entity = CampaignNoteEntity.findById(noteId);
        return Optional.ofNullable(entity).map(mapper::toCampaignNote);
    }
}
