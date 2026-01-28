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
@Table(name = "messages")
public class MessageEntity extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    public ConversationEntity conversation;

    @Column(name = "sender_id", nullable = false)
    public Long senderId;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    public String content;

    @Column(name = "message_type", length = 20, nullable = false)
    public String messageType;

    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    @Column(name = "edited_at")
    public LocalDateTime editedAt;

    @Column(name = "deleted_at")
    public LocalDateTime deletedAt;

    public MessageEntity() {
        this.createdAt = LocalDateTime.now();
    }

    public ConversationEntity getConversation() {
        return conversation;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public String getMessageType() {
        return messageType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getEditedAt() {
        return editedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }
}
