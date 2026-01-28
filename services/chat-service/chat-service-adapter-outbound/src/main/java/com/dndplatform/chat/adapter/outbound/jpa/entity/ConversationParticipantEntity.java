package com.dndplatform.chat.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversation_participants")
public class ConversationParticipantEntity extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    public ConversationEntity conversation;

    @Column(name = "user_id", nullable = false)
    public Long userId;

    @Column(name = "role", length = 20, nullable = false)
    public String role;

    @Column(name = "joined_at", nullable = false, updatable = false)
    public LocalDateTime joinedAt;

    @Column(name = "left_at")
    public LocalDateTime leftAt;

    @Column(name = "last_read_at")
    public LocalDateTime lastReadAt;

    public ConversationParticipantEntity() {
        this.joinedAt = LocalDateTime.now();
    }

    public ConversationEntity getConversation() {
        return conversation;
    }

    public Long getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public LocalDateTime getLeftAt() {
        return leftAt;
    }

    public LocalDateTime getLastReadAt() {
        return lastReadAt;
    }
}
