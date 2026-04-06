package com.dndplatform.campaign.adapter.outbound.jpa.mapper;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignMemberEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignNoteEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignQuestEntity;
import com.dndplatform.campaign.domain.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CampaignEntityMapperTest {

    private final CampaignEntityMapper sut = new CampaignEntityMapper();

    // ---- toCampaign ----

    @Test
    void toCampaign_shouldMapAllFields() {
        CampaignEntity entity = new CampaignEntity();
        entity.id = 42L;
        entity.name = "The Lost Mine";
        entity.description = "A classic adventure";
        entity.dungeonMasterId = 7L;
        entity.status = "ACTIVE";
        entity.maxPlayers = 5;
        entity.imageUrl = "https://example.com/img.png";
        entity.createdAt = LocalDateTime.of(2024, 1, 10, 12, 0);
        entity.updatedAt = LocalDateTime.of(2024, 2, 20, 18, 30);

        Campaign result = sut.toCampaign(entity);

        assertThat(result.id()).isEqualTo(42L);
        assertThat(result.name()).isEqualTo("The Lost Mine");
        assertThat(result.description()).isEqualTo("A classic adventure");
        assertThat(result.dungeonMasterId()).isEqualTo(7L);
        assertThat(result.status()).isEqualTo(CampaignStatus.ACTIVE);
        assertThat(result.maxPlayers()).isEqualTo(5);
        assertThat(result.imageUrl()).isEqualTo("https://example.com/img.png");
        assertThat(result.createdAt()).isEqualTo(LocalDateTime.of(2024, 1, 10, 12, 0));
        assertThat(result.updatedAt()).isEqualTo(LocalDateTime.of(2024, 2, 20, 18, 30));
    }

    @Test
    void toCampaign_shouldMapNullUpdatedAt() {
        CampaignEntity entity = new CampaignEntity();
        entity.id = 1L;
        entity.name = "Draft Campaign";
        entity.description = "desc";
        entity.dungeonMasterId = 1L;
        entity.status = "DRAFT";
        entity.maxPlayers = 6;
        entity.imageUrl = null;
        entity.createdAt = LocalDateTime.now();
        entity.updatedAt = null;

        Campaign result = sut.toCampaign(entity);

        assertThat(result.updatedAt()).isNull();
        assertThat(result.imageUrl()).isNull();
        assertThat(result.status()).isEqualTo(CampaignStatus.DRAFT);
    }

    @Test
    void toCampaign_shouldMapAllCampaignStatuses() {
        for (CampaignStatus expectedStatus : CampaignStatus.values()) {
            CampaignEntity entity = new CampaignEntity();
            entity.id = 1L;
            entity.name = "Campaign";
            entity.description = "desc";
            entity.dungeonMasterId = 1L;
            entity.status = expectedStatus.name();
            entity.maxPlayers = 6;
            entity.createdAt = LocalDateTime.now();

            Campaign result = sut.toCampaign(entity);

            assertThat(result.status()).isEqualTo(expectedStatus);
        }
    }

    // ---- toCampaignMember ----

    @Test
    void toCampaignMember_shouldMapAllFields() {
        CampaignEntity campaign = new CampaignEntity();
        campaign.id = 10L;

        CampaignMemberEntity entity = new CampaignMemberEntity();
        entity.id = 99L;
        entity.campaign = campaign;
        entity.userId = 5L;
        entity.characterId = 3L;
        entity.role = "DUNGEON_MASTER";
        entity.joinedAt = LocalDateTime.of(2024, 3, 15, 9, 0);

        CampaignMember result = sut.toCampaignMember(entity);

        assertThat(result.id()).isEqualTo(99L);
        assertThat(result.campaignId()).isEqualTo(10L);
        assertThat(result.userId()).isEqualTo(5L);
        assertThat(result.characterId()).isEqualTo(3L);
        assertThat(result.role()).isEqualTo(MemberRole.DUNGEON_MASTER);
        assertThat(result.joinedAt()).isEqualTo(LocalDateTime.of(2024, 3, 15, 9, 0));
    }

    @Test
    void toCampaignMember_shouldMapPlayerRole() {
        CampaignEntity campaign = new CampaignEntity();
        campaign.id = 1L;

        CampaignMemberEntity entity = new CampaignMemberEntity();
        entity.id = 1L;
        entity.campaign = campaign;
        entity.userId = 2L;
        entity.characterId = null;
        entity.role = "PLAYER";
        entity.joinedAt = LocalDateTime.now();

        CampaignMember result = sut.toCampaignMember(entity);

        assertThat(result.role()).isEqualTo(MemberRole.PLAYER);
        assertThat(result.characterId()).isNull();
    }

    // ---- toCampaignNote ----

    @Test
    void toCampaignNote_shouldMapAllFields() {
        CampaignEntity campaign = new CampaignEntity();
        campaign.id = 20L;

        CampaignNoteEntity entity = new CampaignNoteEntity();
        entity.id = 55L;
        entity.campaign = campaign;
        entity.authorId = 8L;
        entity.title = "Session 1 Notes";
        entity.content = "The party met at the tavern...";
        entity.visibility = "PUBLIC";
        entity.createdAt = LocalDateTime.of(2024, 4, 1, 10, 0);
        entity.updatedAt = LocalDateTime.of(2024, 4, 2, 11, 0);

        CampaignNote result = sut.toCampaignNote(entity);

        assertThat(result.id()).isEqualTo(55L);
        assertThat(result.campaignId()).isEqualTo(20L);
        assertThat(result.authorId()).isEqualTo(8L);
        assertThat(result.title()).isEqualTo("Session 1 Notes");
        assertThat(result.content()).isEqualTo("The party met at the tavern...");
        assertThat(result.visibility()).isEqualTo(NoteVisibility.PUBLIC);
        assertThat(result.createdAt()).isEqualTo(LocalDateTime.of(2024, 4, 1, 10, 0));
        assertThat(result.updatedAt()).isEqualTo(LocalDateTime.of(2024, 4, 2, 11, 0));
    }

    @Test
    void toCampaignNote_shouldMapPrivateVisibility() {
        CampaignEntity campaign = new CampaignEntity();
        campaign.id = 1L;

        CampaignNoteEntity entity = new CampaignNoteEntity();
        entity.id = 1L;
        entity.campaign = campaign;
        entity.authorId = 1L;
        entity.title = "DM Secret";
        entity.content = "Hidden info";
        entity.visibility = "PRIVATE";
        entity.createdAt = LocalDateTime.now();
        entity.updatedAt = null;

        CampaignNote result = sut.toCampaignNote(entity);

        assertThat(result.visibility()).isEqualTo(NoteVisibility.PRIVATE);
        assertThat(result.updatedAt()).isNull();
    }

    // ---- toCampaignQuest ----

    @Test
    void toCampaignQuest_shouldMapAllFields() {
        CampaignEntity campaign = new CampaignEntity();
        campaign.id = 30L;

        CampaignQuestEntity entity = new CampaignQuestEntity();
        entity.id = 77L;
        entity.campaign = campaign;
        entity.authorId = 11L;
        entity.title = "Retrieve the Artifact";
        entity.description = "Find the lost relic in the dungeon";
        entity.status = "ACTIVE";
        entity.priority = "MAIN";
        entity.createdAt = LocalDateTime.of(2024, 5, 1, 8, 0);
        entity.updatedAt = LocalDateTime.of(2024, 5, 5, 9, 0);

        CampaignQuest result = sut.toCampaignQuest(entity);

        assertThat(result.id()).isEqualTo(77L);
        assertThat(result.campaignId()).isEqualTo(30L);
        assertThat(result.authorId()).isEqualTo(11L);
        assertThat(result.title()).isEqualTo("Retrieve the Artifact");
        assertThat(result.description()).isEqualTo("Find the lost relic in the dungeon");
        assertThat(result.status()).isEqualTo(QuestStatus.ACTIVE);
        assertThat(result.priority()).isEqualTo(QuestPriority.MAIN);
        assertThat(result.createdAt()).isEqualTo(LocalDateTime.of(2024, 5, 1, 8, 0));
        assertThat(result.updatedAt()).isEqualTo(LocalDateTime.of(2024, 5, 5, 9, 0));
    }

    @Test
    void toCampaignQuest_shouldMapAllQuestStatuses() {
        for (QuestStatus expectedStatus : QuestStatus.values()) {
            CampaignEntity campaign = new CampaignEntity();
            campaign.id = 1L;

            CampaignQuestEntity entity = new CampaignQuestEntity();
            entity.id = 1L;
            entity.campaign = campaign;
            entity.authorId = 1L;
            entity.title = "Quest";
            entity.description = "desc";
            entity.status = expectedStatus.name();
            entity.priority = "SIDE";
            entity.createdAt = LocalDateTime.now();

            CampaignQuest result = sut.toCampaignQuest(entity);

            assertThat(result.status()).isEqualTo(expectedStatus);
        }
    }

    @Test
    void toCampaignQuest_shouldMapAllQuestPriorities() {
        for (QuestPriority expectedPriority : QuestPriority.values()) {
            CampaignEntity campaign = new CampaignEntity();
            campaign.id = 1L;

            CampaignQuestEntity entity = new CampaignQuestEntity();
            entity.id = 1L;
            entity.campaign = campaign;
            entity.authorId = 1L;
            entity.title = "Quest";
            entity.description = "desc";
            entity.status = "ACTIVE";
            entity.priority = expectedPriority.name();
            entity.createdAt = LocalDateTime.now();

            CampaignQuest result = sut.toCampaignQuest(entity);

            assertThat(result.priority()).isEqualTo(expectedPriority);
        }
    }

    // ---- toCampaignSummary ----

    @Test
    void toCampaignSummary_shouldMapAllFieldsWithMembers() {
        CampaignEntity entity = new CampaignEntity();
        entity.id = 50L;
        entity.name = "Epic Campaign";
        entity.status = "ACTIVE";

        CampaignMemberEntity m1 = new CampaignMemberEntity();
        CampaignMemberEntity m2 = new CampaignMemberEntity();
        CampaignMemberEntity m3 = new CampaignMemberEntity();
        entity.members = List.of(m1, m2, m3);

        CampaignSummary result = sut.toCampaignSummary(entity);

        assertThat(result.id()).isEqualTo(50L);
        assertThat(result.name()).isEqualTo("Epic Campaign");
        assertThat(result.status()).isEqualTo(CampaignStatus.ACTIVE);
        assertThat(result.playerCount()).isEqualTo(3);
    }

    @Test
    void toCampaignSummary_shouldReturnZeroPlayerCountWhenMembersIsNull() {
        CampaignEntity entity = new CampaignEntity();
        entity.id = 51L;
        entity.name = "Empty Campaign";
        entity.status = "DRAFT";
        entity.members = null;

        CampaignSummary result = sut.toCampaignSummary(entity);

        assertThat(result.playerCount()).isEqualTo(0);
    }

    @Test
    void toCampaignSummary_shouldReturnZeroPlayerCountWhenMembersIsEmpty() {
        CampaignEntity entity = new CampaignEntity();
        entity.id = 52L;
        entity.name = "No Members";
        entity.status = "PAUSED";
        entity.members = List.of();

        CampaignSummary result = sut.toCampaignSummary(entity);

        assertThat(result.playerCount()).isEqualTo(0);
        assertThat(result.status()).isEqualTo(CampaignStatus.PAUSED);
    }
}
