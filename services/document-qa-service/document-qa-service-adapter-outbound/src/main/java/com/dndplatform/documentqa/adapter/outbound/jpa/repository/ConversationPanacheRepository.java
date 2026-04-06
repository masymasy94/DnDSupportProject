package com.dndplatform.documentqa.adapter.outbound.jpa.repository;

import com.dndplatform.documentqa.adapter.outbound.jpa.entity.ConversationEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ConversationPanacheRepository implements PanacheRepository<ConversationEntity> {
    public List<ConversationEntity> findByUserId(Long userId) {
        return find("userId", userId).list();
    }
}
