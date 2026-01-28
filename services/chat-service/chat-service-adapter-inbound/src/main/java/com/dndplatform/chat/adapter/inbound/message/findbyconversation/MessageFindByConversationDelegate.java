package com.dndplatform.chat.adapter.inbound.message.findbyconversation;

import com.dndplatform.chat.adapter.inbound.message.mapper.MessageViewModelMapper;
import com.dndplatform.chat.domain.MessageFindByConversationService;
import com.dndplatform.chat.view.model.MessageFindByConversationResource;
import com.dndplatform.chat.view.model.vm.PagedMessageViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class MessageFindByConversationDelegate implements MessageFindByConversationResource {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 50;

    private final MessageFindByConversationService service;
    private final MessageViewModelMapper mapper;

    @Inject
    public MessageFindByConversationDelegate(MessageFindByConversationService service,
                                             MessageViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public PagedMessageViewModel findByConversation(Long conversationId, Long userId, Integer page, Integer pageSize) {
        int effectivePage = page != null ? page : DEFAULT_PAGE;
        int effectivePageSize = pageSize != null ? pageSize : DEFAULT_PAGE_SIZE;

        var result = service.findByConversationId(conversationId, userId, effectivePage, effectivePageSize);

        return new PagedMessageViewModel(
                result.content().stream().map(mapper).toList(),
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages()
        );
    }
}
