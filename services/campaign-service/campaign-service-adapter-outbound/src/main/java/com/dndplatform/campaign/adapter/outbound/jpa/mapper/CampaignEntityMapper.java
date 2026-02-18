package com.dndplatform.campaign.adapter.outbound.jpa.mapper;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignMemberEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignNoteEntity;
import com.dndplatform.campaign.domain.model.*;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CampaignEntityMapper {

    public Campaign toCampaign(CampaignEntity entity) {
        return CampaignBuilder.builder()
                .withId(entity.id)
                .withName(entity.name)
                .withDescription(entity.description)
                .withDungeonMasterId(entity.dungeonMasterId)
                .withStatus(CampaignStatus.valueOf(entity.status))
                .withMaxPlayers(entity.maxPlayers)
                .withImageUrl(entity.imageUrl)
                .withCreatedAt(entity.createdAt)
                .withUpdatedAt(entity.updatedAt)
                .build();
    }

    public CampaignMember toCampaignMember(CampaignMemberEntity entity) {
        return CampaignMemberBuilder.builder()
                .withId(entity.id)
                .withCampaignId(entity.campaign.id)
                .withUserId(entity.userId)
                .withCharacterId(entity.characterId)
                .withRole(MemberRole.valueOf(entity.role))
                .withJoinedAt(entity.joinedAt)
                .build();
    }

    public CampaignNote toCampaignNote(CampaignNoteEntity entity) {
        return CampaignNoteBuilder.builder()
                .withId(entity.id)
                .withCampaignId(entity.campaign.id)
                .withAuthorId(entity.authorId)
                .withTitle(entity.title)
                .withContent(entity.content)
                .withVisibility(NoteVisibility.valueOf(entity.visibility))
                .withCreatedAt(entity.createdAt)
                .withUpdatedAt(entity.updatedAt)
                .build();
    }

    public CampaignSummary toCampaignSummary(CampaignEntity entity) {
        int playerCount = entity.members != null ? entity.members.size() : 0;
        return CampaignSummaryBuilder.builder()
                .withId(entity.id)
                .withName(entity.name)
                .withStatus(CampaignStatus.valueOf(entity.status))
                .withPlayerCount(playerCount)
                .build();
    }
}
