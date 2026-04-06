package com.dndplatform.documentqa.adapter.outbound.llm;

import com.dndplatform.documentqa.domain.model.ConversationMessage;
import com.dndplatform.documentqa.domain.repository.LlmChatRepository;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class LlmChatRepositoryImpl implements LlmChatRepository {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final DynamicChatModelProvider dynamicChatModelProvider;

    @Inject
    public LlmChatRepositoryImpl(DynamicChatModelProvider dynamicChatModelProvider) {
        this.dynamicChatModelProvider = dynamicChatModelProvider;
    }

    @Override
    public String chat(Long userId, String systemPrompt, List<ConversationMessage> history,
                       String context, String question) {
        log.info(() -> "Sending chat request for user %d with %d history messages".formatted(userId, history.size()));

        ChatModel model = dynamicChatModelProvider.getModel(userId);

        List<ChatMessage> messages = new ArrayList<>();

        // System message
        messages.add(SystemMessage.from(systemPrompt));

        // History messages (exclude the current question which was already added)
        for (ConversationMessage msg : history) {
            if ("USER".equals(msg.role())) {
                messages.add(UserMessage.from(msg.content()));
            } else if ("ASSISTANT".equals(msg.role())) {
                messages.add(AiMessage.from(msg.content()));
            }
        }

        // User message with context prepended
        String userContent = context != null && !context.isEmpty()
                ? "Context:\n" + context + "\n\nQuestion: " + question
                : question;
        messages.add(UserMessage.from(userContent));

        ChatResponse response = model.chat(messages);
        String answer = response.aiMessage().text();

        log.info(() -> "Received LLM response of %d characters".formatted(answer.length()));
        return answer;
    }
}
