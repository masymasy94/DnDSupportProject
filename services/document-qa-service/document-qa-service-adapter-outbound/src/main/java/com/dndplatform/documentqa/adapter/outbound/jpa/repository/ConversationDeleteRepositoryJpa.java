package com.dndplatform.documentqa.adapter.outbound.jpa.repository;

import com.dndplatform.documentqa.adapter.outbound.jpa.entity.ConversationEntity;
import com.dndplatform.documentqa.domain.repository.ConversationDeleteRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class ConversationDeleteRepositoryJpa implements ConversationDeleteRepository {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final ConversationPanacheRepository panacheRepository;

    @Inject
    public ConversationDeleteRepositoryJpa(ConversationPanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info(() -> "Deleting conversation: %d".formatted(id));

        ConversationEntity entity = panacheRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Conversation not found with ID: %d".formatted(id)));

        panacheRepository.delete(entity);
        log.info(() -> "Conversation %d deleted successfully".formatted(id));
    }
}
