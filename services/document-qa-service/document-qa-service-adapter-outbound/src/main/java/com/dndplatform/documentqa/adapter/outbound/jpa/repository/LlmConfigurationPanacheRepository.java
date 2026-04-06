package com.dndplatform.documentqa.adapter.outbound.jpa.repository;

import com.dndplatform.documentqa.adapter.outbound.jpa.entity.LlmConfigurationEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LlmConfigurationPanacheRepository implements PanacheRepository<LlmConfigurationEntity> {
    public List<LlmConfigurationEntity> findSystemConfigurations() {
        return find("userId IS NULL").list();
    }

    public List<LlmConfigurationEntity> findUserConfigurations(Long userId) {
        return find("userId", userId).list();
    }

    public Optional<LlmConfigurationEntity> findActiveSystemConfiguration() {
        return find("userId IS NULL AND active = true").<LlmConfigurationEntity>firstResultOptional();
    }

    public Optional<LlmConfigurationEntity> findActiveUserConfiguration(Long userId) {
        return find("userId = ?1 AND active = true", userId).<LlmConfigurationEntity>firstResultOptional();
    }
}
