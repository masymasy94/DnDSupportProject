package com.dndplatform.chat.adapter.inbound.websocket;

import com.dndplatform.chat.domain.DiceRollService;
import com.dndplatform.chat.domain.MessageSendService;
import com.dndplatform.chat.domain.model.DiceRollResult;
import com.dndplatform.chat.domain.model.Message;
import com.dndplatform.chat.domain.model.MessageType;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import jakarta.json.bind.Jsonb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ChatWebSocketMessageHandlerTest {

    @Mock
    private MessageSendService messageSendService;

    @Mock
    private DiceRollService diceRollService;

    @Mock
    private Jsonb jsonb;

    private ChatWebSocketMessageHandler sut;

    @BeforeEach
    void setUp() {
        sut = new ChatWebSocketMessageHandler(messageSendService, diceRollService, jsonb);
    }

    @Test
    void shouldSendMessageAndReturnResult(@Random Message savedMessage) {
        Long userId = 1L;
        Long conversationId = 10L;
        String content = "Hello world";

        given(messageSendService.send(conversationId, userId, content, MessageType.TEXT)).willReturn(savedMessage);

        ChatWebSocketMessage result = sut.handleSendMessage(userId, conversationId, content, null);

        assertThat(result.type()).isEqualTo("NEW_MESSAGE");
        assertThat(result.conversationId()).isEqualTo(savedMessage.conversationId());
        assertThat(result.messageId()).isEqualTo(savedMessage.id());
        assertThat(result.senderId()).isEqualTo(savedMessage.senderId());
        assertThat(result.content()).isEqualTo(savedMessage.content());
    }

    @Test
    void shouldSendMessageWithExplicitMessageType(@Random Message savedMessage) {
        Long userId = 1L;
        Long conversationId = 10L;

        given(messageSendService.send(conversationId, userId, "hello", MessageType.TEXT)).willReturn(savedMessage);

        ChatWebSocketMessage result = sut.handleSendMessage(userId, conversationId, "hello", "TEXT");

        assertThat(result.type()).isEqualTo("NEW_MESSAGE");
    }

    @Test
    void shouldReturnErrorWhenConversationIdMissing() {
        ChatWebSocketMessage result = sut.handleSendMessage(1L, null, "hello", null);

        assertThat(result.type()).isEqualTo("ERROR");
        assertThat(result.content()).isEqualTo("conversationId is required");
        then(messageSendService).shouldHaveNoInteractions();
    }

    @Test
    void shouldReturnErrorWhenContentMissing() {
        ChatWebSocketMessage result = sut.handleSendMessage(1L, 10L, null, null);

        assertThat(result.type()).isEqualTo("ERROR");
        assertThat(result.content()).isEqualTo("content is required");
    }

    @Test
    void shouldReturnErrorWhenContentBlank() {
        ChatWebSocketMessage result = sut.handleSendMessage(1L, 10L, "  ", null);

        assertThat(result.type()).isEqualTo("ERROR");
        assertThat(result.content()).isEqualTo("content is required");
    }

    @Test
    void shouldReturnErrorOnIllegalArgumentInSendMessage() {
        Long userId = 1L;
        Long conversationId = 10L;

        given(messageSendService.send(conversationId, userId, "hello", MessageType.TEXT))
                .willThrow(new IllegalArgumentException("User not in conversation"));

        ChatWebSocketMessage result = sut.handleSendMessage(userId, conversationId, "hello", null);

        assertThat(result.type()).isEqualTo("ERROR");
        assertThat(result.content()).isEqualTo("User not in conversation");
    }

    @Test
    void shouldRollDiceAndReturnResult(@Random Message savedMessage, @Random DiceRollResult diceResult) {
        Long userId = 1L;
        Long conversationId = 10L;
        String formula = "2d6+3";

        given(diceRollService.roll(formula)).willReturn(diceResult);
        given(jsonb.toJson(diceResult)).willReturn("{\"formula\":\"2d6+3\"}");
        given(messageSendService.send(eq(conversationId), eq(userId), anyString(), eq(MessageType.DICE_ROLL))).willReturn(savedMessage);

        ChatWebSocketMessage result = sut.handleRollDice(userId, conversationId, formula);

        assertThat(result.type()).isEqualTo("NEW_MESSAGE");
        assertThat(result.conversationId()).isEqualTo(savedMessage.conversationId());

        then(messageSendService).should().send(conversationId, userId, "{\"formula\":\"2d6+3\"}", MessageType.DICE_ROLL);
    }

    @Test
    void shouldReturnErrorWhenDiceConversationIdMissing() {
        ChatWebSocketMessage result = sut.handleRollDice(1L, null, "2d6");

        assertThat(result.type()).isEqualTo("ERROR");
        assertThat(result.content()).isEqualTo("conversationId is required");
    }

    @Test
    void shouldReturnErrorWhenDiceFormulaMissing() {
        ChatWebSocketMessage result = sut.handleRollDice(1L, 10L, null);

        assertThat(result.type()).isEqualTo("ERROR");
        assertThat(result.content()).isEqualTo("content is required (dice formula)");
    }

    @Test
    void shouldReturnErrorWhenDiceFormulaBlank() {
        ChatWebSocketMessage result = sut.handleRollDice(1L, 10L, "  ");

        assertThat(result.type()).isEqualTo("ERROR");
        assertThat(result.content()).isEqualTo("content is required (dice formula)");
    }

    @Test
    void shouldReturnErrorOnInvalidDiceFormula() {
        given(diceRollService.roll("invalid")).willThrow(new IllegalArgumentException("Bad formula"));

        ChatWebSocketMessage result = sut.handleRollDice(1L, 10L, "invalid");

        assertThat(result.type()).isEqualTo("ERROR");
        assertThat(result.content()).isEqualTo("Invalid dice formula: Bad formula");
    }

    @Test
    void shouldReturnErrorOnDiceRollRuntimeException() {
        given(diceRollService.roll("2d6")).willThrow(new RuntimeException("Unexpected"));

        ChatWebSocketMessage result = sut.handleRollDice(1L, 10L, "2d6");

        assertThat(result.type()).isEqualTo("ERROR");
        assertThat(result.content()).isEqualTo("Dice roll failed: Unexpected");
    }
}
