package com.dndplatform.documentqa.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "llm_configurations")
public class LlmConfigurationEntity extends PanacheEntity {

    @Column(name = "user_id")
    public Long userId;

    @Column(name = "name", length = 100, nullable = false)
    public String name;

    @Column(name = "provider", length = 50, nullable = false)
    public String provider;

    @Column(name = "model_name", length = 100, nullable = false)
    public String modelName;

    @Column(name = "base_url", length = 500)
    public String baseUrl;

    @Column(name = "api_key_encrypted", length = 1000)
    public String apiKeyEncrypted;

    @Column(name = "embedding_provider", length = 50)
    public String embeddingProvider;

    @Column(name = "embedding_model", length = 100)
    public String embeddingModel;

    @Column(name = "active", nullable = false)
    public boolean active;

    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    public LlmConfigurationEntity() {
        this.createdAt = LocalDateTime.now();
        this.active = false;
    }
}
