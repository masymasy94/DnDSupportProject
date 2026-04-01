package com.dndplatform.documentqa.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversation_messages")
public class ConversationMessageEntity extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    public ConversationEntity conversation;

    @Column(name = "role", length = 20, nullable = false)
    public String role;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    public String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    public ConversationMessageEntity() {
        this.createdAt = LocalDateTime.now();
    }
}
