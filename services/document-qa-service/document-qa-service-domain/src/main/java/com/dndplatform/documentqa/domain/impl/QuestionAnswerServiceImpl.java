package com.dndplatform.documentqa.domain.impl;

import com.dndplatform.documentqa.domain.QuestionAnswerService;
import com.dndplatform.documentqa.domain.model.*;
import com.dndplatform.documentqa.domain.repository.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
public class QuestionAnswerServiceImpl implements QuestionAnswerService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final ConversationCreateRepository conversationCreateRepository;
    private final ConversationFindRepository conversationFindRepository;
    private final ConversationMessageCreateRepository messageCreateRepository;
    private final ConversationMessageFindRepository messageFindRepository;
    private final DocumentChunkSearchRepository chunkSearchRepository;
    private final EmbeddingRepository embeddingRepository;
    private final LlmChatRepository llmChatRepository;
    private final SystemPromptProvider systemPromptProvider;

    @Inject
    public QuestionAnswerServiceImpl(ConversationCreateRepository conversationCreateRepository,
                                     ConversationFindRepository conversationFindRepository,
                                     ConversationMessageCreateRepository messageCreateRepository,
                                     ConversationMessageFindRepository messageFindRepository,
                                     DocumentChunkSearchRepository chunkSearchRepository,
                                     EmbeddingRepository embeddingRepository,
                                     LlmChatRepository llmChatRepository,
                                     SystemPromptProvider systemPromptProvider) {
        this.conversationCreateRepository = conversationCreateRepository;
        this.conversationFindRepository = conversationFindRepository;
        this.messageCreateRepository = messageCreateRepository;
        this.messageFindRepository = messageFindRepository;
        this.chunkSearchRepository = chunkSearchRepository;
        this.embeddingRepository = embeddingRepository;
        this.llmChatRepository = llmChatRepository;
        this.systemPromptProvider = systemPromptProvider;
    }

    @Override
    public QuestionAnswer ask(QuestionRequest request) {
        // Get or create conversation
        Long conversationId = request.conversationId();
        if (conversationId == null) {
            String title = request.question().length() > 80
                    ? request.question().substring(0, 80) + "..."
                    : request.question();
            Conversation conversation = conversationCreateRepository.create(request.userId(), null, title);
            conversationId = conversation.id();
        }

        // Save user message
        messageCreateRepository.create(conversationId, "USER", request.question());

        // Get conversation history
        List<ConversationMessage> history = messageFindRepository.findByConversationId(conversationId);

        // Search for relevant chunks
        float[] queryEmbedding = embeddingRepository.embed(request.question());
        List<SourceReference> sources = chunkSearchRepository.searchSimilar(
                queryEmbedding, request.documentIds(), 5);

        // Build context from sources
        String context = sources.stream()
                .map(s -> "[%s] %s".formatted(s.fileName(), s.excerpt()))
                .collect(Collectors.joining("\n\n"));

        // Get LLM response
        String systemPrompt = systemPromptProvider.getSystemPrompt();
        String answer = llmChatRepository.chat(
                request.userId(), systemPrompt, history, context, request.question());

        // Save assistant message
        messageCreateRepository.create(conversationId, "ASSISTANT", answer);

        log.info(() -> "Answered question for user %d in conversation %d with %d sources"
                .formatted(request.userId(), request.conversationId() != null ? request.conversationId() : -1, sources.size()));

        return QuestionAnswerBuilder.builder()
                .withConversationId(conversationId)
                .withAnswer(answer)
                .withSources(sources)
                .build();
    }
}
