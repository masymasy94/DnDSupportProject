package com.dndplatform.chat.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conversations")
public class ConversationEntity extends PanacheEntity {

    @Column(name = "name", length = 100)
    public String name;

    @Column(name = "type", length = 20, nullable = false)
    public String type;

    @Column(name = "created_by", nullable = false)
    public Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    public List<ConversationParticipantEntity> participants = new ArrayList<>();

    public ConversationEntity() {
        this.createdAt = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<ConversationParticipantEntity> getParticipants() {
        return participants;
    }
}
