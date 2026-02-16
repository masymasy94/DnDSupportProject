package com.dndplatform.chat.adapter.outbound.jpa.repository;

import com.dndplatform.chat.adapter.outbound.jpa.mapper.MessageMapper;
import com.dndplatform.chat.domain.model.Message;
import com.dndplatform.chat.domain.model.PagedResult;
import com.dndplatform.chat.domain.repository.MessageFindByConversationRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class MessageFindByConversationRepositoryImpl implements MessageFindByConversationRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final MessagePanacheRepository panacheRepository;
    private final MessageMapper mapper;

    @Inject
    public MessageFindByConversationRepositoryImpl(MessagePanacheRepository panacheRepository,
                                                    MessageMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public PagedResult<Message> findByConversationId(Long conversationId, int page, int pageSize) {
        log.info(() -> "Finding messages: conversationId=%d, page=%d, pageSize=%d"
                .formatted(conversationId, page, pageSize));

        var query = panacheRepository.find("conversation.id = ?1 AND deletedAt IS NULL",
                Sort.by("createdAt").descending(),
                conversationId);

        long totalElements = query.count();

        var content = query
                .page(page, pageSize)
                .list()
                .stream()
                .map(mapper)
                .toList();

        return new PagedResult<>(content, page, pageSize, totalElements);
    }
}
