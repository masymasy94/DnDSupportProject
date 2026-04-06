package com.dndplatform.chat.adapter.inbound.websocket;

import com.dndplatform.chat.domain.DiceRollService;
import com.dndplatform.chat.domain.MessageSendService;
import com.dndplatform.chat.domain.model.DiceRollResult;
import com.dndplatform.chat.domain.model.Message;
import com.dndplatform.chat.domain.model.MessageType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;

import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class ChatWebSocketMessageHandler {

    private static final Logger log = Logger.getLogger(ChatWebSocketMessageHandler.class.getName());

    private final MessageSendService messageSendService;
    private final DiceRollService diceRollService;
    private final Jsonb jsonb;

    @Inject
    public ChatWebSocketMessageHandler(MessageSendService messageSendService,
                                       DiceRollService diceRollService,
                                       Jsonb jsonb) {
        this.messageSendService = messageSendService;
        this.diceRollService = diceRollService;
        this.jsonb = jsonb;
    }

    public ChatWebSocketMessage handleSendMessage(Long senderId, Long conversationId, String content, String messageType) {
        if (conversationId == null) {
            return ChatWebSocketMessage.error("conversationId is required");
        }
        if (content == null || content.isBlank()) {
            return ChatWebSocketMessage.error("content is required");
        }

        try {
            MessageType type = messageType != null
                    ? MessageType.valueOf(messageType)
                    : MessageType.TEXT;

            Message savedMessage = messageSendService.send(conversationId, senderId, content, type);
            return toOutgoingMessage(savedMessage);

        } catch (IllegalArgumentException e) {
            return ChatWebSocketMessage.error(e.getMessage());
        }
    }

    public ChatWebSocketMessage handleRollDice(Long userId, Long conversationId, String formula) {
        if (conversationId == null) {
            return ChatWebSocketMessage.error("conversationId is required");
        }
        if (formula == null || formula.isBlank()) {
            return ChatWebSocketMessage.error("content is required (dice formula)");
        }

        try {
            DiceRollResult result = diceRollService.roll(formula);
            String jsonContent = jsonb.toJson(result);

            Message savedMessage = messageSendService.send(conversationId, userId, jsonContent, MessageType.DICE_ROLL);
            return toOutgoingMessage(savedMessage);

        } catch (IllegalArgumentException e) {
            return ChatWebSocketMessage.error("Invalid dice formula: " + e.getMessage());
        } catch (Exception e) {
            log.log(Level.WARNING, "Dice roll failed", e);
            return ChatWebSocketMessage.error("Dice roll failed: " + e.getMessage());
        }
    }

    private ChatWebSocketMessage toOutgoingMessage(Message savedMessage) {
        return ChatWebSocketMessage.newMessage(
                savedMessage.conversationId(),
                savedMessage.id(),
                savedMessage.senderId(),
                savedMessage.content(),
                savedMessage.messageType().name(),
                savedMessage.createdAt()
        );
    }
}
