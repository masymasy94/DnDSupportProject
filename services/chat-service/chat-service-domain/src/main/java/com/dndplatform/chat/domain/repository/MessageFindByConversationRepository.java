package com.dndplatform.chat.domain.repository;

import com.dndplatform.chat.domain.model.Message;
import com.dndplatform.chat.domain.model.PagedResult;

public interface MessageFindByConversationRepository {
    PagedResult<Message> findByConversationId(Long conversationId, int page, int pageSize);
}
