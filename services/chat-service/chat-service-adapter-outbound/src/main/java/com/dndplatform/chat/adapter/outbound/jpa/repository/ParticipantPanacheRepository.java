package com.dndplatform.chat.adapter.outbound.jpa.repository;

import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationParticipantEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ParticipantPanacheRepository implements PanacheRepository<ConversationParticipantEntity> {
}
