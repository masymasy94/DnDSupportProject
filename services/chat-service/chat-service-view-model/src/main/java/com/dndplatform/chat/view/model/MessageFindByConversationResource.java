package com.dndplatform.chat.view.model;

import com.dndplatform.chat.view.model.vm.PagedMessageViewModel;

public interface MessageFindByConversationResource {
    PagedMessageViewModel findByConversation(Long conversationId, Long userId, Integer page, Integer pageSize);
}
