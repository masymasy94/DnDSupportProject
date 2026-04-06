package com.dndplatform.documentqa.domain.impl;

import com.dndplatform.documentqa.domain.model.*;
import com.dndplatform.documentqa.domain.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("QuestionAnswerServiceImpl")
class QuestionAnswerServiceImplTest {

    @Mock
    private ConversationCreateRepository conversationCreateRepository;

    @Mock
    private ConversationFindRepository conversationFindRepository;

    @Mock
    private ConversationMessageCreateRepository messageCreateRepository;

    @Mock
    private ConversationMessageFindRepository messageFindRepository;

    @Mock
    private DocumentChunkSearchRepository chunkSearchRepository;

    @Mock
    private EmbeddingRepository embeddingRepository;

    @Mock
    private LlmChatRepository llmChatRepository;

    @Mock
    private SystemPromptProvider systemPromptProvider;

    private QuestionAnswerServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new QuestionAnswerServiceImpl(
                conversationCreateRepository,
                conversationFindRepository,
                messageCreateRepository,
                messageFindRepository,
                chunkSearchRepository,
                embeddingRepository,
                llmChatRepository,
                systemPromptProvider
        );
    }

    @Nested
    @DisplayName("ask")
    class Ask {

        @Test
        @DisplayName("should create new conversation and answer when no conversationId provided")
        void shouldCreateNewConversation_WhenNoConversationId() {
            Long userId = 100L;
            String question = "What is this document about?";
            List<String> documentIds = List.of("doc-1", "doc-2");

            Conversation newConversation = new Conversation(1L, userId, null, "What is this", null, null);
            given(conversationCreateRepository.create(eq(userId), isNull(), anyString())).willReturn(newConversation);

            given(messageFindRepository.findByConversationId(1L)).willReturn(List.of());

            float[] embedding = new float[]{0.1f, 0.2f};
            given(embeddingRepository.embed(question)).willReturn(embedding);

            List<SourceReference> sources = List.of(
                    new SourceReference("doc-1", "file1.pdf", "This is the content...", 0.9)
            );
            given(chunkSearchRepository.searchSimilar(embedding, documentIds, 5)).willReturn(sources);

            given(systemPromptProvider.getSystemPrompt()).willReturn("You are a helpful assistant.");

            given(llmChatRepository.chat(eq(userId), anyString(), anyList(), anyString(), eq(question)))
                    .willReturn("This document describes the main features of the system.");

            ConversationMessage userMsg = new ConversationMessage(1L, 1L, "USER", question, null);
            ConversationMessage assistantMsg = new ConversationMessage(2L, 1L, "ASSISTANT", "This document describes...", null);
            given(messageCreateRepository.create(1L, "USER", question)).willReturn(userMsg);
            given(messageCreateRepository.create(1L, "ASSISTANT", "This document describes the main features of the system.")).willReturn(assistantMsg);

            QuestionRequest request = new QuestionRequest(userId, null, question, documentIds);
            QuestionAnswer result = sut.ask(request);

            assertThat(result.conversationId()).isEqualTo(1L);
            assertThat(result.answer()).isEqualTo("This document describes the main features of the system.");
            assertThat(result.sources()).isEqualTo(sources);

            then(conversationCreateRepository).should().create(eq(userId), isNull(), anyString());
            then(messageCreateRepository).should().create(1L, "USER", question);
            then(embeddingRepository).should().embed(question);
            then(chunkSearchRepository).should().searchSimilar(embedding, documentIds, 5);
            then(llmChatRepository).should().chat(eq(userId), anyString(), anyList(), anyString(), eq(question));
            then(messageCreateRepository).should().create(1L, "ASSISTANT", "This document describes the main features of the system.");
        }

        @Test
        @DisplayName("should use existing conversation when conversationId provided")
        void shouldUseExistingConversation_WhenConversationIdProvided() {
            Long userId = 100L;
            Long conversationId = 5L;
            String question = "What are the key points?";
            List<String> documentIds = List.of("doc-1");

            given(messageFindRepository.findByConversationId(conversationId)).willReturn(List.of(
                    new ConversationMessage(1L, conversationId, "USER", "Previous question?", null),
                    new ConversationMessage(2L, conversationId, "ASSISTANT", "Previous answer", null)
            ));

            float[] embedding = new float[]{0.1f, 0.2f};
            given(embeddingRepository.embed(question)).willReturn(embedding);

            List<SourceReference> sources = List.of();
            given(chunkSearchRepository.searchSimilar(embedding, documentIds, 5)).willReturn(sources);

            given(systemPromptProvider.getSystemPrompt()).willReturn("You are a helpful assistant.");

            given(llmChatRepository.chat(eq(userId), anyString(), anyList(), anyString(), eq(question)))
                    .willReturn("The key points are...");

            ConversationMessage userMsg = new ConversationMessage(3L, conversationId, "USER", question, null);
            ConversationMessage assistantMsg = new ConversationMessage(4L, conversationId, "ASSISTANT", "The key points are...", null);
            given(messageCreateRepository.create(conversationId, "USER", question)).willReturn(userMsg);
            given(messageCreateRepository.create(conversationId, "ASSISTANT", "The key points are...")).willReturn(assistantMsg);

            QuestionRequest request = new QuestionRequest(userId, conversationId, question, documentIds);
            QuestionAnswer result = sut.ask(request);

            assertThat(result.conversationId()).isEqualTo(conversationId);
            then(conversationCreateRepository).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("should truncate long question for title when creating new conversation")
        void shouldTruncateLongQuestion_ForTitleWhenCreatingNewConversation() {
            Long userId = 100L;
            String longQuestion = "A".repeat(100);
            String expectedTitle = "A".repeat(80) + "...";

            Conversation newConversation = new Conversation(1L, userId, null, expectedTitle, null, null);
            given(conversationCreateRepository.create(eq(userId), isNull(), eq(expectedTitle))).willReturn(newConversation);

            given(messageFindRepository.findByConversationId(1L)).willReturn(List.of());

            given(embeddingRepository.embed(longQuestion)).willReturn(new float[]{0.1f});
            given(chunkSearchRepository.searchSimilar(any(), anyList(), anyInt())).willReturn(List.of());
            given(systemPromptProvider.getSystemPrompt()).willReturn("You are a helpful assistant.");
            given(llmChatRepository.chat(anyLong(), anyString(), anyList(), anyString(), anyString()))
                    .willReturn("Answer");

            given(messageCreateRepository.create(1L, "USER", longQuestion)).willReturn(new ConversationMessage(1L, 1L, "USER", longQuestion, null));
            given(messageCreateRepository.create(1L, "ASSISTANT", "Answer")).willReturn(new ConversationMessage(2L, 1L, "ASSISTANT", "Answer", null));

            QuestionRequest request = new QuestionRequest(userId, null, longQuestion, List.of("doc-1"));
            sut.ask(request);

            then(conversationCreateRepository).should().create(eq(userId), isNull(), eq(expectedTitle));
        }
    }
}