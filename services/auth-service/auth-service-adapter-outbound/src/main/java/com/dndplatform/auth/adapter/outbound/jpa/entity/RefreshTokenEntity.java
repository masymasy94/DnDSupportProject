package com.dndplatform.auth.adapter.outbound.jpa.entity;

import com.dndplatform.common.annotations.Builder;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "refresh_tokens")
public class RefreshTokenEntity extends PanacheEntity {

    @Column(name = "token", length = 255, nullable = false, unique = true)
    public String token;

    @Column(name = "user_id", nullable = false)
    public Long userId;

    @Column(name = "expires_at", nullable = false)
    public LocalDateTime expiresAt;

    @Column(name = "revoked", nullable = false)
    public Boolean revoked = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    public RefreshTokenEntity() {
        this.createdAt = LocalDateTime.now();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getRevoked() {
        return revoked;
    }

    public void setRevoked(Boolean revoked) {
        this.revoked = revoked;
    }
}