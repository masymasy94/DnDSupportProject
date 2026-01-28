package com.dndplatform.chat.domain;

import com.dndplatform.chat.domain.model.Message;
import com.dndplatform.chat.domain.model.PagedResult;

public interface MessageFindByConversationService {
    PagedResult<Message> findByConversationId(Long conversationId, Long userId, int page, int pageSize);
}
