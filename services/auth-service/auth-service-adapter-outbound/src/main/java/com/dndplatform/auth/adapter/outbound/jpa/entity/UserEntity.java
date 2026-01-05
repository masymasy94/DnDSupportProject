package com.dndplatform.auth.adapter.outbound.jpa.entity;

import com.dndplatform.common.annotations.Builder;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "users")
public class UserEntity extends PanacheEntity {

    @Column(name = "username", length = 50, nullable = false, unique = true)
    public String username;

    @Column(name = "email", length = 255, nullable = false, unique = true)
    public String email;

    @Column(name = "password_hash", length = 255, nullable = false)
    public String passwordHash;

    @Column(name = "role", length = 20, nullable = false)
    public String role = "PLAYER";

    @Column(name = "active", nullable = false)
    public Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    @Column(name = "last_login")
    public LocalDateTime lastLogin;

    public UserEntity() {
        this.createdAt = LocalDateTime.now();
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
}