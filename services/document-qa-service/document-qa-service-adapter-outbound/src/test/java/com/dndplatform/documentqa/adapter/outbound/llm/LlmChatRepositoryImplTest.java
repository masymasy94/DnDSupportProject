package com.dndplatform.documentqa.adapter.outbound.llm;

import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.documentqa.domain.model.ConversationMessage;
import com.dndplatform.documentqa.domain.model.ConversationMessageBuilder;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@DisplayName("LlmChatRepositoryImpl")
class LlmChatRepositoryImplTest {

    @Mock
    private DynamicChatModelProvider dynamicChatModelProvider;

    @Mock
    private ChatModel chatModel;

    private LlmChatRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new LlmChatRepositoryImpl(dynamicChatModelProvider);
    }

    private ChatResponse mockResponse(String text) {
        ChatResponse response = mock(ChatResponse.class);
        AiMessage aiMessage = mock(AiMessage.class);
        given(response.aiMessage()).willReturn(aiMessage);
        given(aiMessage.text()).willReturn(text);
        return response;
    }

    private ConversationMessage userMsg(String content) {
        return ConversationMessageBuilder.builder()
                .withId(1L)
                .withConversationId(10L)
                .withRole("USER")
                .withContent(content)
                .withCreatedAt(LocalDateTime.now())
                .build();
    }

    private ConversationMessage assistantMsg(String content) {
        return ConversationMessageBuilder.builder()
                .withId(2L)
                .withConversationId(10L)
                .withRole("ASSISTANT")
                .withContent(content)
                .withCreatedAt(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("chat")
    class Chat {

        @Test
        @DisplayName("should return LLM answer when called without history or context")
        void shouldReturnLlmAnswerWhenCalledWithoutHistoryOrContext() {
            Long userId = 1L;
            String systemPrompt = "You are a DnD game master assistant.";
            String question = "What is a beholder?";
            String expectedAnswer = "A beholder is a floating orb with many eyes.";

            ChatResponse response = mockResponse(expectedAnswer);
            given(dynamicChatModelProvider.getModel(userId)).willReturn(chatModel);
            given(chatModel.chat(anyList())).willReturn(response);

            String result = sut.chat(userId, systemPrompt, List.of(), null, question);

            assertThat(result).isEqualTo(expectedAnswer);
        }

        @Test
        @DisplayName("should send system message as first message")
        void shouldSendSystemMessageAsFirstMessage() {
            Long userId = 1L;
            ChatResponse response = mockResponse("answer");
            given(dynamicChatModelProvider.getModel(userId)).willReturn(chatModel);
            given(chatModel.chat(anyList())).willReturn(response);

            sut.chat(userId, "System prompt", List.of(), null, "Question?");

            @SuppressWarnings("unchecked")
            ArgumentCaptor<List<ChatMessage>> captor = ArgumentCaptor.forClass(List.class);
            then(chatModel).should().chat(captor.capture());

            List<ChatMessage> sentMessages = captor.getValue();
            assertThat(sentMessages).isNotEmpty();
            assertThat(sentMessages.get(0)).isInstanceOf(SystemMessage.class);
        }

        @Test
        @DisplayName("should send exactly 2 messages when no history and no context")
        void shouldSendExactly2MessagesWhenNoHistoryAndNoContext() {
            Long userId = 2L;
            ChatResponse response = mockResponse("answer");
            given(dynamicChatModelProvider.getModel(userId)).willReturn(chatModel);
            given(chatModel.chat(anyList())).willReturn(response);

            sut.chat(userId, "System.", List.of(), null, "Question?");

            @SuppressWarnings("unchecked")
            ArgumentCaptor<List<ChatMessage>> captor = ArgumentCaptor.forClass(List.class);
            then(chatModel).should().chat(captor.capture());

            // 1 system + 1 user question = 2
            assertThat(captor.getValue()).hasSize(2);
            assertThat(captor.getValue().get(1)).isInstanceOf(UserMessage.class);
        }

        @Test
        @DisplayName("should include history messages and produce correct message count")
        void shouldIncludeHistoryMessagesInCorrectOrder() {
            Long userId = 3L;
            String systemPrompt = "You are an oracle.";
            List<ConversationMessage> history = List.of(
                    userMsg("What is a lich?"),
                    assistantMsg("A lich is an undead sorcerer."),
                    userMsg("Can a paladin defeat one?")
            );

            ChatResponse response = mockResponse("Yes, with holy magic.");
            given(dynamicChatModelProvider.getModel(userId)).willReturn(chatModel);
            given(chatModel.chat(anyList())).willReturn(response);

            String result = sut.chat(userId, systemPrompt, history, null, "How exactly?");

            assertThat(result).isEqualTo("Yes, with holy magic.");

            @SuppressWarnings("unchecked")
            ArgumentCaptor<List<ChatMessage>> captor = ArgumentCaptor.forClass(List.class);
            then(chatModel).should().chat(captor.capture());

            List<ChatMessage> sentMessages = captor.getValue();
            // system + 3 history messages + 1 current question = 5
            assertThat(sentMessages).hasSize(5);
            assertThat(sentMessages.get(0)).isInstanceOf(SystemMessage.class);
            assertThat(sentMessages.get(1)).isInstanceOf(UserMessage.class);
            assertThat(sentMessages.get(2)).isInstanceOf(AiMessage.class);
            assertThat(sentMessages.get(3)).isInstanceOf(UserMessage.class);
            assertThat(sentMessages.get(4)).isInstanceOf(UserMessage.class);
        }

        @Test
        @DisplayName("should include context in user message when context is non-empty")
        void shouldIncludeContextInUserMessageWhenContextNonEmpty() {
            Long userId = 4L;
            String context = "Dragons are ancient creatures.";
            String question = "How old do dragons live?";

            ChatResponse response = mockResponse("Very long.");
            given(dynamicChatModelProvider.getModel(userId)).willReturn(chatModel);
            given(chatModel.chat(anyList())).willReturn(response);

            sut.chat(userId, "System.", List.of(), context, question);

            @SuppressWarnings("unchecked")
            ArgumentCaptor<List<ChatMessage>> captor = ArgumentCaptor.forClass(List.class);
            then(chatModel).should().chat(captor.capture());

            List<ChatMessage> sentMessages = captor.getValue();
            // The last message should be a UserMessage with context prepended
            assertThat(sentMessages).hasSize(2);
            assertThat(sentMessages.get(1)).isInstanceOf(UserMessage.class);
            // toString should include the content text
            assertThat(sentMessages.get(1).toString()).contains(context);
        }

        @Test
        @DisplayName("should get chat model from provider before sending request")
        void shouldGetChatModelFromProviderBeforeSendingRequest() {
            Long userId = 5L;
            ChatResponse response = mockResponse("Answer.");
            given(dynamicChatModelProvider.getModel(userId)).willReturn(chatModel);
            given(chatModel.chat(anyList())).willReturn(response);

            sut.chat(userId, "System.", List.of(), null, "Question?");

            InOrder order = inOrder(dynamicChatModelProvider, chatModel);
            order.verify(dynamicChatModelProvider).getModel(userId);
            order.verify(chatModel).chat(anyList());
        }
    }
}
