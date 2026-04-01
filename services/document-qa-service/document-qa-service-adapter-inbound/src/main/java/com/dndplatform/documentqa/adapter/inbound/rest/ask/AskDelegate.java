package com.dndplatform.documentqa.adapter.inbound.rest.ask;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.documentqa.domain.QuestionAnswerService;
import com.dndplatform.documentqa.domain.model.QuestionAnswer;
import com.dndplatform.documentqa.domain.model.QuestionRequestBuilder;
import com.dndplatform.documentqa.view.model.AskResource;
import com.dndplatform.documentqa.view.model.vm.*;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

@Delegate
@RequestScoped
public class AskDelegate implements AskResource {

    private final QuestionAnswerService service;

    @Inject
    public AskDelegate(QuestionAnswerService service) {
        this.service = service;
    }

    @Override
    public QuestionAnswerViewModel ask(AskRequest request) {
        var domainRequest = QuestionRequestBuilder.builder()
                .withUserId(request.userId())
                .withConversationId(request.conversationId())
                .withQuestion(request.question())
                .withDocumentIds(request.documentIds())
                .build();

        QuestionAnswer answer = service.ask(domainRequest);

        List<SourceReferenceViewModel> sources = answer.sources().stream()
                .map(s -> SourceReferenceViewModelBuilder.builder()
                        .withDocumentId(s.documentId())
                        .withFileName(s.fileName())
                        .withExcerpt(s.excerpt())
                        .withScore(s.score())
                        .build())
                .toList();

        return QuestionAnswerViewModelBuilder.builder()
                .withConversationId(answer.conversationId())
                .withAnswer(answer.answer())
                .withSources(sources)
                .build();
    }
}
