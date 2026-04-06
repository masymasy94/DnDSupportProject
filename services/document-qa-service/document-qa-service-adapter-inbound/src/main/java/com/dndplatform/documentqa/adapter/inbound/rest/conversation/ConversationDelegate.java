package com.dndplatform.documentqa.adapter.inbound.rest.conversation;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.documentqa.domain.ConversationDeleteService;
import com.dndplatform.documentqa.domain.ConversationFindService;
import com.dndplatform.documentqa.domain.ConversationHistoryService;
import com.dndplatform.documentqa.view.model.ConversationResource;
import com.dndplatform.documentqa.view.model.vm.ConversationMessageViewModel;
import com.dndplatform.documentqa.view.model.vm.ConversationMessageViewModelBuilder;
import com.dndplatform.documentqa.view.model.vm.ConversationViewModel;
import com.dndplatform.documentqa.view.model.vm.ConversationViewModelBuilder;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

@Delegate
@RequestScoped
public class ConversationDelegate implements ConversationResource {

    private final ConversationFindService findService;
    private final ConversationHistoryService historyService;
    private final ConversationDeleteService deleteService;

    @Inject
    public ConversationDelegate(ConversationFindService findService,
                                ConversationHistoryService historyService,
                                ConversationDeleteService deleteService) {
        this.findService = findService;
        this.historyService = historyService;
        this.deleteService = deleteService;
    }

    @Override
    public List<ConversationViewModel> listConversations(Long userId) {
        return findService.findByUserId(userId).stream()
                .map(c -> ConversationViewModelBuilder.builder()
                        .withId(c.id())
                        .withUserId(c.userId())
                        .withCampaignId(c.campaignId())
                        .withTitle(c.title())
                        .withCreatedAt(c.createdAt())
                        .withUpdatedAt(c.updatedAt())
                        .build())
                .toList();
    }

    @Override
    public ConversationViewModel getConversation(Long id, Long userId) {
        var c = findService.findById(id, userId);
        return ConversationViewModelBuilder.builder()
                .withId(c.id())
                .withUserId(c.userId())
                .withCampaignId(c.campaignId())
                .withTitle(c.title())
                .withCreatedAt(c.createdAt())
                .withUpdatedAt(c.updatedAt())
                .build();
    }

    @Override
    public List<ConversationMessageViewModel> getMessages(Long id, Long userId) {
        return historyService.getHistory(id, userId).stream()
                .map(m -> ConversationMessageViewModelBuilder.builder()
                        .withId(m.id())
                        .withRole(m.role())
                        .withContent(m.content())
                        .withCreatedAt(m.createdAt())
                        .build())
                .toList();
    }

    @Override
    public void deleteConversation(Long id, Long userId) {
        deleteService.delete(id, userId);
    }
}
