# Testability Refactoring: ChatWebSocketEndpoint & PdfCharacterSheetGenerator

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Extract business logic from ChatWebSocketEndpoint and PdfCharacterSheetGenerator into independently testable classes, then write comprehensive tests.

**Architecture:** Extract-and-delegate pattern. Each monolithic class is split into a thin adapter (keeps framework coupling) and a pure-logic class (easy to unit-test). Follows existing hexagonal architecture patterns in the codebase.

**Tech Stack:** Java 25, JUnit 5, Mockito, AssertJ, common-test RandomExtension, Apache PDFBox 3.0.4

---

## File Structure

### Chat Service — WebSocket Refactoring

| Action | File |
|--------|------|
| Create | `services/chat-service/chat-service-adapter-inbound/src/main/java/com/dndplatform/chat/adapter/inbound/websocket/ChatWebSocketMessageHandler.java` |
| Modify | `services/chat-service/chat-service-adapter-inbound/src/main/java/com/dndplatform/chat/adapter/inbound/websocket/ChatWebSocketEndpoint.java` |
| Create | `services/chat-service/chat-service-adapter-inbound/src/test/java/com/dndplatform/chat/adapter/inbound/websocket/ChatWebSocketMessageHandlerTest.java` |

### Character Service — PDF Refactoring

| Action | File |
|--------|------|
| Create | `services/character-service/character-service-adapter-inbound/src/main/java/com/dndplatform/character/adapter/inbound/create/generator/CharacterFieldMapper.java` |
| Create | `services/character-service/character-service-adapter-inbound/src/main/java/com/dndplatform/character/adapter/inbound/create/generator/CharacterFieldMap.java` |
| Modify | `services/character-service/character-service-adapter-inbound/src/main/java/com/dndplatform/character/adapter/inbound/create/generator/PdfCharacterSheetGenerator.java` |
| Create | `services/character-service/character-service-adapter-inbound/src/test/java/com/dndplatform/character/adapter/inbound/create/generator/CharacterFieldMapperTest.java` |
| Create | `services/character-service/character-service-adapter-inbound/src/test/java/com/dndplatform/character/adapter/inbound/create/generator/PdfCharacterSheetGeneratorTest.java` |

---

### Task 1: Create ChatWebSocketMessageHandler — failing tests

**Files:**
- Create: `services/chat-service/chat-service-adapter-inbound/src/test/java/com/dndplatform/chat/adapter/inbound/websocket/ChatWebSocketMessageHandlerTest.java`

- [ ] **Step 1: Write the test class with all test cases**

```java
package com.dndplatform.chat.adapter.inbound.websocket;

import com.dndplatform.chat.domain.DiceRollService;
import com.dndplatform.chat.domain.MessageSendService;
import com.dndplatform.chat.domain.model.ConversationParticipant;
import com.dndplatform.chat.domain.model.DiceRollResult;
import com.dndplatform.chat.domain.model.Message;
import com.dndplatform.chat.domain.model.MessageType;
import com.dndplatform.chat.domain.repository.ParticipantFindByConversationRepository;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ChatWebSocketMessageHandlerTest {

    @Mock
    private MessageSendService messageSendService;

    @Mock
    private ParticipantFindByConversationRepository participantRepository;

    @Mock
    private ChatSessionManager sessionManager;

    @Mock
    private DiceRollService diceRollService;

    private ChatWebSocketMessageHandler sut;

    @BeforeEach
    void setUp() {
        sut = new ChatWebSocketMessageHandler(messageSendService, participantRepository, sessionManager, diceRollService);
    }

    @Test
    void shouldSendMessageAndBroadcast(@Random Message savedMessage, @Random ConversationParticipant participant) {
        Long userId = 1L;
        Long conversationId = 10L;
        String content = "Hello world";

        given(messageSendService.send(conversationId, userId, content, MessageType.TEXT)).willReturn(savedMessage);
        given(participantRepository.findByConversationId(conversationId)).willReturn(List.of(participant));

        ChatWebSocketMessage result = sut.handleSendMessage(userId, conversationId, content, null);

        assertThat(result.type()).isEqualTo("NEW_MESSAGE");
        assertThat(result.conversationId()).isEqualTo(savedMessage.conversationId());
        assertThat(result.messageId()).isEqualTo(savedMessage.id());
        assertThat(result.senderId()).isEqualTo(savedMessage.senderId());
        assertThat(result.content()).isEqualTo(savedMessage.content());

        then(sessionManager).should().broadcastToUsers(eq(List.of(participant.userId())), anyString());
    }

    @Test
    void shouldSendMessageWithExplicitMessageType(@Random Message savedMessage, @Random ConversationParticipant participant) {
        Long userId = 1L;
        Long conversationId = 10L;

        given(messageSendService.send(conversationId, userId, "hello", MessageType.TEXT)).willReturn(savedMessage);
        given(participantRepository.findByConversationId(conversationId)).willReturn(List.of(participant));

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
    void shouldRollDiceAndBroadcast(@Random Message savedMessage, @Random DiceRollResult diceResult, @Random ConversationParticipant participant) {
        Long userId = 1L;
        Long conversationId = 10L;
        String formula = "2d6+3";

        given(diceRollService.roll(formula)).willReturn(diceResult);
        given(messageSendService.send(eq(conversationId), eq(userId), anyString(), eq(MessageType.DICE_ROLL))).willReturn(savedMessage);
        given(participantRepository.findByConversationId(conversationId)).willReturn(List.of(participant));

        ChatWebSocketMessage result = sut.handleRollDice(userId, conversationId, formula);

        assertThat(result.type()).isEqualTo("NEW_MESSAGE");
        assertThat(result.conversationId()).isEqualTo(savedMessage.conversationId());

        then(sessionManager).should().broadcastToUsers(eq(List.of(participant.userId())), anyString());
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
```

- [ ] **Step 2: Run test to verify it fails (class not found)**

Run: `bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn" test -pl services/chat-service/chat-service-adapter-inbound -Dtest=ChatWebSocketMessageHandlerTest -f /Users/marybookpro/IdeaProjects/DnDSupportProject/pom.xml`

Expected: COMPILATION ERROR — `ChatWebSocketMessageHandler` does not exist.

---

### Task 2: Implement ChatWebSocketMessageHandler

**Files:**
- Create: `services/chat-service/chat-service-adapter-inbound/src/main/java/com/dndplatform/chat/adapter/inbound/websocket/ChatWebSocketMessageHandler.java`

- [ ] **Step 3: Create the handler class**

```java
package com.dndplatform.chat.adapter.inbound.websocket;

import com.dndplatform.chat.domain.DiceRollService;
import com.dndplatform.chat.domain.MessageSendService;
import com.dndplatform.chat.domain.model.DiceRollResult;
import com.dndplatform.chat.domain.model.Message;
import com.dndplatform.chat.domain.model.MessageType;
import com.dndplatform.chat.domain.repository.ParticipantFindByConversationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class ChatWebSocketMessageHandler {

    private static final Logger log = Logger.getLogger(ChatWebSocketMessageHandler.class.getName());
    private final Jsonb jsonb = JsonbBuilder.create();

    private final MessageSendService messageSendService;
    private final ParticipantFindByConversationRepository participantRepository;
    private final ChatSessionManager sessionManager;
    private final DiceRollService diceRollService;

    @Inject
    public ChatWebSocketMessageHandler(MessageSendService messageSendService,
                                       ParticipantFindByConversationRepository participantRepository,
                                       ChatSessionManager sessionManager,
                                       DiceRollService diceRollService) {
        this.messageSendService = messageSendService;
        this.participantRepository = participantRepository;
        this.sessionManager = sessionManager;
        this.diceRollService = diceRollService;
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
            ChatWebSocketMessage outgoing = toOutgoingMessage(savedMessage);
            broadcastToParticipants(conversationId, outgoing);
            return outgoing;

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
            ChatWebSocketMessage outgoing = toOutgoingMessage(savedMessage);
            broadcastToParticipants(conversationId, outgoing);
            return outgoing;

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

    private void broadcastToParticipants(Long conversationId, ChatWebSocketMessage outgoing) {
        List<Long> participantIds = participantRepository.findByConversationId(conversationId)
                .stream()
                .map(p -> p.userId())
                .toList();
        sessionManager.broadcastToUsers(participantIds, jsonb.toJson(outgoing));
    }
}
```

- [ ] **Step 4: Run tests to verify they pass**

Run: `bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn" test -pl services/chat-service/chat-service-adapter-inbound -Dtest=ChatWebSocketMessageHandlerTest -f /Users/marybookpro/IdeaProjects/DnDSupportProject/pom.xml`

Expected: All 12 tests PASS.

- [ ] **Step 5: Commit**

```bash
git add services/chat-service/chat-service-adapter-inbound/src/main/java/com/dndplatform/chat/adapter/inbound/websocket/ChatWebSocketMessageHandler.java \
      services/chat-service/chat-service-adapter-inbound/src/test/java/com/dndplatform/chat/adapter/inbound/websocket/ChatWebSocketMessageHandlerTest.java
git commit -m "feat: extract ChatWebSocketMessageHandler with tests"
```

---

### Task 3: Refactor ChatWebSocketEndpoint to use handler

**Files:**
- Modify: `services/chat-service/chat-service-adapter-inbound/src/main/java/com/dndplatform/chat/adapter/inbound/websocket/ChatWebSocketEndpoint.java`

- [ ] **Step 6: Replace business logic in ChatWebSocketEndpoint with delegation to handler**

Replace the entire file with:

```java
package com.dndplatform.chat.adapter.inbound.websocket;

import io.quarkus.websockets.next.OnClose;
import io.quarkus.websockets.next.OnError;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebSocket(path = "/ws/chat")
public class ChatWebSocketEndpoint {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final Jsonb jsonb = JsonbBuilder.create();

    private final Map<String, Long> connectionUserMap = new ConcurrentHashMap<>();

    private final ChatSessionManager sessionManager;
    private final JWTParser jwtParser;
    private final ChatWebSocketMessageHandler messageHandler;

    @Inject
    public ChatWebSocketEndpoint(ChatSessionManager sessionManager,
                                 JWTParser jwtParser,
                                 ChatWebSocketMessageHandler messageHandler) {
        this.sessionManager = sessionManager;
        this.jwtParser = jwtParser;
        this.messageHandler = messageHandler;
    }

    @OnOpen
    public void onOpen(WebSocketConnection connection) {
        log.info(() -> "WebSocket connection opened: " + connection.id());

        String token = parseQueryParam(connection.handshakeRequest().query(), "token");

        if (token == null || token.isBlank()) {
            connection.sendTextAndAwait(jsonb.toJson(ChatWebSocketMessage.error("Missing authentication token")));
            return;
        }

        try {
            JsonWebToken jwt = jwtParser.parse(token);
            Long userId = extractUserId(jwt);

            if (userId == null) {
                connection.sendTextAndAwait(jsonb.toJson(ChatWebSocketMessage.error("Invalid token: unable to extract user ID")));
                return;
            }

            connectionUserMap.put(connection.id(), userId);
            sessionManager.addConnection(userId, connection);
            connection.sendTextAndAwait(jsonb.toJson(ChatWebSocketMessage.connected()));

        } catch (ParseException e) {
            log.warning(() -> "Failed to parse JWT: " + e.getMessage());
            connection.sendTextAndAwait(jsonb.toJson(ChatWebSocketMessage.error("Invalid authentication token")));
        }
    }

    @OnClose
    public void onClose(WebSocketConnection connection) {
        log.info(() -> "WebSocket connection closed: " + connection.id());
        Long userId = connectionUserMap.remove(connection.id());
        if (userId != null) {
            sessionManager.removeConnection(userId, connection);
        }
    }

    @OnError
    public void onError(WebSocketConnection connection, Throwable throwable) {
        log.log(Level.WARNING, "WebSocket error for connection " + connection.id(), throwable);
        Long userId = connectionUserMap.remove(connection.id());
        if (userId != null) {
            sessionManager.removeConnection(userId, connection);
        }
    }

    @OnTextMessage
    @Blocking
    public String onMessage(String message, WebSocketConnection connection) {
        Long userId = connectionUserMap.get(connection.id());
        if (userId == null) {
            return jsonb.toJson(ChatWebSocketMessage.error("Session not authenticated"));
        }

        try {
            ChatWebSocketIncomingMessage incoming = jsonb.fromJson(message, ChatWebSocketIncomingMessage.class);

            ChatWebSocketMessage result;
            if ("SEND_MESSAGE".equals(incoming.type())) {
                result = messageHandler.handleSendMessage(userId, incoming.conversationId(), incoming.content(), incoming.messageType());
            } else if ("ROLL_DICE".equals(incoming.type())) {
                result = messageHandler.handleRollDice(userId, incoming.conversationId(), incoming.content());
            } else {
                result = ChatWebSocketMessage.error("Unknown message type: " + incoming.type());
            }
            return jsonb.toJson(result);
        } catch (Exception e) {
            log.log(Level.WARNING, "Failed to process message", e);
            return jsonb.toJson(ChatWebSocketMessage.error("Failed to process message: " + e.getMessage()));
        }
    }

    private Long extractUserId(JsonWebToken jwt) {
        Object userIdClaim = jwt.getClaim("userId");
        if (userIdClaim != null) {
            if (userIdClaim instanceof Number) {
                return ((Number) userIdClaim).longValue();
            }
            return Long.parseLong(userIdClaim.toString());
        }

        String subject = jwt.getSubject();
        if (subject != null) {
            try {
                return Long.parseLong(subject);
            } catch (NumberFormatException e) {
                // Subject might be username, not user ID
            }
        }
        return null;
    }

    private String parseQueryParam(String queryString, String paramName) {
        if (queryString == null || queryString.isBlank()) {
            return null;
        }
        for (String param : queryString.split("&")) {
            String[] keyValue = param.split("=", 2);
            if (keyValue.length == 2 && keyValue[0].equals(paramName)) {
                return keyValue[1];
            }
        }
        return null;
    }
}
```

- [ ] **Step 7: Run all chat-service adapter-inbound tests**

Run: `bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn" test -pl services/chat-service/chat-service-adapter-inbound -f /Users/marybookpro/IdeaProjects/DnDSupportProject/pom.xml`

Expected: All tests PASS (existing + new).

- [ ] **Step 8: Commit**

```bash
git add services/chat-service/chat-service-adapter-inbound/src/main/java/com/dndplatform/chat/adapter/inbound/websocket/ChatWebSocketEndpoint.java
git commit -m "refactor: thin ChatWebSocketEndpoint, delegate to ChatWebSocketMessageHandler"
```

---

### Task 4: Create CharacterFieldMap record and CharacterFieldMapper — failing tests

**Files:**
- Create: `services/character-service/character-service-adapter-inbound/src/test/java/com/dndplatform/character/adapter/inbound/create/generator/CharacterFieldMapperTest.java`

- [ ] **Step 9: Write the test class**

```java
package com.dndplatform.character.adapter.inbound.create.generator;

import com.dndplatform.character.domain.model.*;
import com.dndplatform.character.domain.model.Character;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CharacterFieldMapperTest {

    private CharacterFieldMapper sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterFieldMapper();
    }

    @Test
    void shouldMapCharacterName() {
        Character character = characterWithName("Gandalf");

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("CharacterName", "Gandalf");
        assertThat(result.textFields()).containsEntry("CharacterName 2", "Gandalf");
    }

    @Test
    void shouldMapClassAndLevel() {
        Character character = new CharacterBuilder()
                .withCharacterClass("Wizard")
                .withLevel(5)
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("ClassLevel", "Wizard 5");
    }

    @Test
    void shouldMapBasicInfo() {
        Character character = new CharacterBuilder()
                .withSpecies("Elf")
                .withBackground("Sage")
                .withAlignment("Neutral Good")
                .withExperiencePoints(6500)
                .withProficiencyBonus(3)
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("Race", "Elf");
        assertThat(result.textFields()).containsEntry("Background", "Sage");
        assertThat(result.textFields()).containsEntry("Alignment", "Neutral Good");
        assertThat(result.textFields()).containsEntry("XP", "6500");
        assertThat(result.textFields()).containsEntry("ProfBonus", "+3");
    }

    @Test
    void shouldMapAbilityScores() {
        Character character = new CharacterBuilder()
                .withAbilityScores(new AbilityScoresBuilder()
                        .withStrength(16).withDexterity(14).withConstitution(12)
                        .withIntelligence(18).withWisdom(10).withCharisma(8)
                        .build())
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("STR", "16");
        assertThat(result.textFields()).containsEntry("DEX", "14");
        assertThat(result.textFields()).containsEntry("INT", "18");
        assertThat(result.textFields()).containsEntry("CHA", "8");
    }

    @Test
    void shouldMapAbilityModifiers() {
        Character character = new CharacterBuilder()
                .withAbilityScores(new AbilityScoresBuilder()
                        .withStrength(16).withDexterity(14).withConstitution(12)
                        .withIntelligence(18).withWisdom(10).withCharisma(8)
                        .build())
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("STRmod", "+3");
        assertThat(result.textFields()).containsEntry("DEXmod", "+2");
        assertThat(result.textFields()).containsEntry("CONmod", "+1");
        assertThat(result.textFields()).containsEntry("INTmod", "+4");
        assertThat(result.textFields()).containsEntry("WISmod", "+0");
        assertThat(result.textFields()).containsEntry("CHamod", "-1");
    }

    @Test
    void shouldMapSavingThrowProficiencies() {
        Character character = new CharacterBuilder()
                .withSavingThrows(List.of(
                        new SavingThrowProficiencyBuilder().withAbility("INT").withProficient(true).withModifier(6).build(),
                        new SavingThrowProficiencyBuilder().withAbility("WIS").withProficient(false).withModifier(0).build()
                ))
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("ST Intelligence", "+6");
        assertThat(result.textFields()).containsEntry("ST Wisdom", "+0");
        assertThat(result.checkboxFields()).containsEntry("Check Box 14", true);
        assertThat(result.checkboxFields()).doesNotContainKey("Check Box 15");
    }

    @Test
    void shouldMapSkillProficiencies() {
        Character character = new CharacterBuilder()
                .withSkills(List.of(
                        new SkillProficiencyBuilder().withName("Arcana").withProficient(true).withModifier(7).build(),
                        new SkillProficiencyBuilder().withName("Stealth").withProficient(false).withModifier(2).build()
                ))
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("Arcana", "+7");
        assertThat(result.textFields()).containsEntry("Stealth", "+2");
        assertThat(result.checkboxFields()).containsEntry("Check Box 25", true);
        assertThat(result.checkboxFields()).doesNotContainKey("Check Box 39");
    }

    @Test
    void shouldMapCombatStats() {
        Character character = new CharacterBuilder()
                .withArmorClass(15)
                .withSpeed(30)
                .withHitPointsMax(45)
                .withHitPointsCurrent(38)
                .withHitPointsTemp(5)
                .withHitDiceTotal(5)
                .withHitDiceType("d10")
                .withAbilityScores(new AbilityScoresBuilder()
                        .withStrength(10).withDexterity(14).withConstitution(10)
                        .withIntelligence(10).withWisdom(10).withCharisma(10)
                        .build())
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("AC", "15");
        assertThat(result.textFields()).containsEntry("Speed", "30");
        assertThat(result.textFields()).containsEntry("HPMax", "45");
        assertThat(result.textFields()).containsEntry("HPCurrent", "38");
        assertThat(result.textFields()).containsEntry("HPTemp", "5");
        assertThat(result.textFields()).containsEntry("HDTotal", "5");
        assertThat(result.textFields()).containsEntry("HD", "d10");
        assertThat(result.textFields()).containsEntry("Initiative", "+2");
    }

    @Test
    void shouldMapPassivePerception() {
        Character character = new CharacterBuilder()
                .withSkills(List.of(
                        new SkillProficiencyBuilder().withName("Perception").withProficient(true).withModifier(5).build()
                ))
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("Passive", "15");
    }

    @Test
    void shouldDefaultPassivePerceptionTo10WhenNoSkills() {
        Character character = new CharacterBuilder().build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("Passive", "10");
    }

    @Test
    void shouldMapSpellcasting() {
        Character character = new CharacterBuilder()
                .withSpellcastingAbility("Intelligence")
                .withCharacterClass("Wizard")
                .withSpellSaveDc(15)
                .withSpellAttackBonus(7)
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("SpellcastingAbility 2", "Intelligence");
        assertThat(result.textFields()).containsEntry("Spellcasting Class 2", "Wizard");
        assertThat(result.textFields()).containsEntry("SpellSaveDC  2", "15");
        assertThat(result.textFields()).containsEntry("SpellAtkBonus 2", "+7");
    }

    @Test
    void shouldMapPersonality() {
        Character character = new CharacterBuilder()
                .withPersonalityTraits("Curious")
                .withIdeals("Knowledge")
                .withBonds("My library")
                .withFlaws("Arrogant")
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("PersonalityTraits", "Curious");
        assertThat(result.textFields()).containsEntry("Ideals", "Knowledge");
        assertThat(result.textFields()).containsEntry("Bonds", "My library");
        assertThat(result.textFields()).containsEntry("Flaws", "Arrogant");
    }

    @Test
    void shouldMapPhysicalCharacteristics() {
        Character character = new CharacterBuilder()
                .withPhysicalCharacteristics(new PhysicalCharacteristicsBuilder()
                        .withAge("150").withHeight("6'0\"").withWeight("180 lbs")
                        .withEyes("Blue").withSkin("Fair").withHair("Silver")
                        .build())
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("Age", "150");
        assertThat(result.textFields()).containsEntry("Height", "6'0\"");
        assertThat(result.textFields()).containsEntry("Eyes", "Blue");
        assertThat(result.textFields()).containsEntry("Hair", "Silver");
    }

    @Test
    void shouldMapEquipment() {
        Character character = new CharacterBuilder()
                .withEquipment(List.of(
                        new EquipmentBuilder().withName("Longsword").withQuantity(1).build(),
                        new EquipmentBuilder().withName("Healing Potion").withQuantity(3).build()
                ))
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("Equipment", "Longsword\nHealing Potion (x3)");
    }

    @Test
    void shouldMapSpellSlots() {
        Character character = new CharacterBuilder()
                .withSpellSlots(List.of(
                        new SpellSlotAllocationBuilder().withSpellLevel(1).withSlotsTotal(4).build(),
                        new SpellSlotAllocationBuilder().withSpellLevel(2).withSlotsTotal(3).build()
                ))
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("SlotsTotal 19", "4");
        assertThat(result.textFields()).containsEntry("SlotsRemaining 19", "4");
        assertThat(result.textFields()).containsEntry("SlotsTotal 20", "3");
        assertThat(result.textFields()).containsEntry("SlotsRemaining 20", "3");
    }

    @Test
    void shouldMapLanguagesAndProficiencies() {
        Character character = new CharacterBuilder()
                .withLanguages(List.of("Common", "Elvish"))
                .withProficiencies(List.of(
                        new ProficiencyBuilder().withType("Armor").withName("Light Armor").build(),
                        new ProficiencyBuilder().withType("Armor").withName("Medium Armor").build(),
                        new ProficiencyBuilder().withType("Weapon").withName("Simple Weapons").build()
                ))
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        String profLang = result.textFields().get("ProficienciesLang");
        assertThat(profLang).contains("Languages: Common, Elvish");
        assertThat(profLang).contains("Armor: Light Armor, Medium Armor");
        assertThat(profLang).contains("Weapon: Simple Weapons");
    }

    @Test
    void shouldHandleNullAbilityScores() {
        Character character = new CharacterBuilder().build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).doesNotContainKey("STR");
        assertThat(result.textFields()).doesNotContainKey("STRmod");
    }

    @Test
    void shouldHandleNullLists() {
        Character character = new CharacterBuilder()
                .withSkills(null)
                .withSavingThrows(null)
                .withEquipment(null)
                .withSpells(null)
                .withSpellSlots(null)
                .withLanguages(null)
                .withProficiencies(null)
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        // Should not throw, should produce valid (possibly sparse) map
        assertThat(result.textFields()).isNotNull();
        assertThat(result.checkboxFields()).isNotNull();
    }

    @Test
    void shouldFormatNegativeModifier() {
        Character character = new CharacterBuilder()
                .withAbilityScores(new AbilityScoresBuilder()
                        .withStrength(8).withDexterity(10).withConstitution(10)
                        .withIntelligence(10).withWisdom(10).withCharisma(10)
                        .build())
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("STRmod", "-1");
    }

    private Character characterWithName(String name) {
        return new CharacterBuilder().withName(name).build();
    }
}
```

- [ ] **Step 10: Run test to verify it fails (class not found)**

Run: `bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn" test -pl services/character-service/character-service-adapter-inbound -Dtest=CharacterFieldMapperTest -f /Users/marybookpro/IdeaProjects/DnDSupportProject/pom.xml`

Expected: COMPILATION ERROR — `CharacterFieldMapper` and `CharacterFieldMap` do not exist.

---

### Task 5: Implement CharacterFieldMap and CharacterFieldMapper

**Files:**
- Create: `services/character-service/character-service-adapter-inbound/src/main/java/com/dndplatform/character/adapter/inbound/create/generator/CharacterFieldMap.java`
- Create: `services/character-service/character-service-adapter-inbound/src/main/java/com/dndplatform/character/adapter/inbound/create/generator/CharacterFieldMapper.java`

- [ ] **Step 11: Create CharacterFieldMap record**

```java
package com.dndplatform.character.adapter.inbound.create.generator;

import java.util.Map;

public record CharacterFieldMap(
        Map<String, String> textFields,
        Map<String, Boolean> checkboxFields
) {}
```

- [ ] **Step 12: Create CharacterFieldMapper**

```java
package com.dndplatform.character.adapter.inbound.create.generator;

import com.dndplatform.character.domain.model.Character;
import com.dndplatform.character.domain.model.*;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class CharacterFieldMapper {

    private static final String[] ABILITY_CODES = {"STR", "DEX", "CON", "INT", "WIS", "CHA"};

    private static final String[] SKILL_NAMES = {
            "Acrobatics", "Animal Handling", "Arcana", "Athletics",
            "Deception", "History", "Insight", "Intimidation",
            "Investigation", "Medicine", "Nature", "Perception",
            "Performance", "Persuasion", "Religion", "Sleight of Hand",
            "Stealth", "Survival"
    };

    private static final String[] SKILL_CHECKBOX_FIELDS = {
            "Check Box 23", "Check Box 24", "Check Box 25", "Check Box 26",
            "Check Box 27", "Check Box 28", "Check Box 29", "Check Box 30",
            "Check Box 31", "Check Box 32", "Check Box 33", "Check Box 34",
            "Check Box 35", "Check Box 36", "Check Box 37", "Check Box 38",
            "Check Box 39", "Check Box 40"
    };

    private static final String[] SKILL_MODIFIER_FIELDS = {
            "Acrobatics", "Animal", "Arcana", "Athletics",
            "Deception", "History", "Insight", "Intimidation",
            "Investigation", "Medicine", "Nature", "Perception",
            "Performance", "Persuasion", "Religion",
            "SleightofHand", "Stealth", "Survival"
    };

    private static final String[] SAVING_THROW_CHECKBOX_FIELDS = {
            "Check Box 11", "Check Box 12", "Check Box 13",
            "Check Box 14", "Check Box 15", "Check Box 16"
    };

    private static final String[] SAVING_THROW_MODIFIER_FIELDS = {
            "ST Strength", "ST Dexterity", "ST Constitution",
            "ST Intelligence", "ST Wisdom", "ST Charisma"
    };

    private static final String[] SAVING_THROW_ABILITY_CODES = {
            "STR", "DEX", "CON", "INT", "WIS", "CHA"
    };

    private static final String[] ABILITY_MODIFIER_FIELDS = {
            "STRmod", "DEXmod", "CONmod", "INTmod", "WISmod", "CHamod"
    };

    public CharacterFieldMap mapFields(Character character) {
        Map<String, String> textFields = new LinkedHashMap<>();
        Map<String, Boolean> checkboxFields = new LinkedHashMap<>();

        fillCharacterInfo(textFields, character);
        fillAbilityScores(textFields, character);
        fillAbilityModifiers(textFields, character);
        fillSavingThrows(textFields, checkboxFields, character);
        fillSkills(textFields, checkboxFields, character);
        fillCombatStats(textFields, character);
        fillSpellcasting(textFields, character);
        fillPersonality(textFields, character);
        fillPhysicalCharacteristics(textFields, character);
        fillEquipment(textFields, character);
        fillSpellSlots(textFields, character);
        fillProficienciesAndLanguages(textFields, character);

        return new CharacterFieldMap(Map.copyOf(textFields), Map.copyOf(checkboxFields));
    }

    private void fillCharacterInfo(Map<String, String> fields, Character character) {
        putIfNotBlank(fields, "CharacterName", character.name());
        putIfNotBlank(fields, "CharacterName 2", character.name());
        putIfNotBlank(fields, "Race", character.species());
        putIfNotBlank(fields, "Background", character.background());
        putIfNotBlank(fields, "Alignment", character.alignment());
        putIfNotBlank(fields, "XP", str(character.experiencePoints()));

        String classLevel = character.characterClass();
        if (classLevel != null && character.level() != null) {
            classLevel = classLevel + " " + character.level();
        }
        putIfNotBlank(fields, "ClassLevel", classLevel);

        putIfNotBlank(fields, "ProfBonus", formatModifier(character.proficiencyBonus()));
    }

    private void fillAbilityScores(Map<String, String> fields, Character character) {
        AbilityScores scores = character.abilityScores();
        if (scores == null) return;

        putIfNotBlank(fields, "STR", str(scores.strength()));
        putIfNotBlank(fields, "DEX", str(scores.dexterity()));
        putIfNotBlank(fields, "CON", str(scores.constitution()));
        putIfNotBlank(fields, "INT", str(scores.intelligence()));
        putIfNotBlank(fields, "WIS", str(scores.wisdom()));
        putIfNotBlank(fields, "CHA", str(scores.charisma()));
    }

    private void fillAbilityModifiers(Map<String, String> fields, Character character) {
        AbilityScores scores = character.abilityScores();
        if (scores == null) return;

        for (int i = 0; i < ABILITY_CODES.length; i++) {
            Integer modifier = scores.getModifier(ABILITY_CODES[i]);
            putIfNotBlank(fields, ABILITY_MODIFIER_FIELDS[i], formatModifier(modifier));
        }
    }

    private void fillSavingThrows(Map<String, String> fields, Map<String, Boolean> checkboxes, Character character) {
        List<SavingThrowProficiency> savingThrows = character.savingThrows();
        if (savingThrows == null) return;

        Map<String, SavingThrowProficiency> stMap = savingThrows.stream()
                .collect(Collectors.toMap(SavingThrowProficiency::ability, st -> st, (a, b) -> a));

        for (int i = 0; i < SAVING_THROW_ABILITY_CODES.length; i++) {
            SavingThrowProficiency st = stMap.get(SAVING_THROW_ABILITY_CODES[i]);
            if (st != null) {
                if (Boolean.TRUE.equals(st.proficient())) {
                    checkboxes.put(SAVING_THROW_CHECKBOX_FIELDS[i], true);
                }
                putIfNotBlank(fields, SAVING_THROW_MODIFIER_FIELDS[i], formatModifier(st.modifier()));
            }
        }
    }

    private void fillSkills(Map<String, String> fields, Map<String, Boolean> checkboxes, Character character) {
        List<SkillProficiency> skills = character.skills();
        if (skills == null) return;

        Map<String, SkillProficiency> skillMap = skills.stream()
                .collect(Collectors.toMap(SkillProficiency::name, s -> s, (a, b) -> a));

        for (int i = 0; i < SKILL_NAMES.length; i++) {
            SkillProficiency skill = skillMap.get(SKILL_NAMES[i]);
            if (skill != null) {
                if (Boolean.TRUE.equals(skill.proficient())) {
                    checkboxes.put(SKILL_CHECKBOX_FIELDS[i], true);
                }
                putIfNotBlank(fields, SKILL_MODIFIER_FIELDS[i], formatModifier(skill.modifier()));
            }
        }
    }

    private void fillCombatStats(Map<String, String> fields, Character character) {
        putIfNotBlank(fields, "AC", str(character.armorClass()));
        putIfNotBlank(fields, "Speed", str(character.speed()));
        putIfNotBlank(fields, "HPMax", str(character.hitPointsMax()));
        putIfNotBlank(fields, "HPCurrent", str(character.hitPointsCurrent()));
        putIfNotBlank(fields, "HPTemp", str(character.hitPointsTemp()));
        putIfNotBlank(fields, "HDTotal", str(character.hitDiceTotal()));
        putIfNotBlank(fields, "HD", character.hitDiceType());
        putIfNotBlank(fields, "Passive", str(calculatePassivePerception(character)));

        if (character.abilityScores() != null) {
            Integer initiative = character.abilityScores().getModifier("DEX");
            putIfNotBlank(fields, "Initiative", formatModifier(initiative));
        }
    }

    private void fillSpellcasting(Map<String, String> fields, Character character) {
        putIfNotBlank(fields, "SpellcastingAbility 2", character.spellcastingAbility());
        putIfNotBlank(fields, "Spellcasting Class 2", character.characterClass());
        putIfNotBlank(fields, "SpellSaveDC  2", str(character.spellSaveDc()));
        putIfNotBlank(fields, "SpellAtkBonus 2", formatModifier(character.spellAttackBonus()));
    }

    private void fillPersonality(Map<String, String> fields, Character character) {
        putIfNotBlank(fields, "PersonalityTraits", character.personalityTraits());
        putIfNotBlank(fields, "Ideals", character.ideals());
        putIfNotBlank(fields, "Bonds", character.bonds());
        putIfNotBlank(fields, "Flaws", character.flaws());
    }

    private void fillPhysicalCharacteristics(Map<String, String> fields, Character character) {
        PhysicalCharacteristics phys = character.physicalCharacteristics();
        if (phys == null) return;

        putIfNotBlank(fields, "Age", phys.age());
        putIfNotBlank(fields, "Height", phys.height());
        putIfNotBlank(fields, "Weight", phys.weight());
        putIfNotBlank(fields, "Eyes", phys.eyes());
        putIfNotBlank(fields, "Skin", phys.skin());
        putIfNotBlank(fields, "Hair", phys.hair());
    }

    private void fillEquipment(Map<String, String> fields, Character character) {
        List<Equipment> equipment = character.equipment();
        if (equipment == null || equipment.isEmpty()) return;

        String text = equipment.stream()
                .map(e -> e.quantity() != null && e.quantity() > 1
                        ? e.name() + " (x" + e.quantity() + ")"
                        : e.name())
                .collect(Collectors.joining("\n"));
        putIfNotBlank(fields, "Equipment", text);
    }

    private void fillSpellSlots(Map<String, String> fields, Character character) {
        List<SpellSlotAllocation> slots = character.spellSlots();
        if (slots == null) return;

        for (SpellSlotAllocation slot : slots) {
            if (slot.spellLevel() != null && slot.spellLevel() >= 1 && slot.spellLevel() <= 9) {
                int fieldNum = 18 + slot.spellLevel();
                putIfNotBlank(fields, "SlotsTotal " + fieldNum, str(slot.slotsTotal()));
                putIfNotBlank(fields, "SlotsRemaining " + fieldNum, str(slot.slotsTotal()));
            }
        }
    }

    private void fillProficienciesAndLanguages(Map<String, String> fields, Character character) {
        List<String> parts = new ArrayList<>();

        if (character.languages() != null && !character.languages().isEmpty()) {
            parts.add("Languages: " + String.join(", ", character.languages()));
        }

        if (character.proficiencies() != null && !character.proficiencies().isEmpty()) {
            Map<String, List<String>> grouped = character.proficiencies().stream()
                    .collect(Collectors.groupingBy(
                            p -> p.type() != null ? p.type() : "Other",
                            Collectors.mapping(Proficiency::name, Collectors.toList())));

            grouped.forEach((type, names) ->
                    parts.add(type + ": " + String.join(", ", names)));
        }

        if (!parts.isEmpty()) {
            putIfNotBlank(fields, "ProficienciesLang", String.join("\n", parts));
        }
    }

    private Integer calculatePassivePerception(Character character) {
        if (character.skills() == null) return 10;

        return character.skills().stream()
                .filter(s -> "Perception".equals(s.name()))
                .findFirst()
                .map(s -> 10 + (s.modifier() != null ? s.modifier() : 0))
                .orElse(10);
    }

    private void putIfNotBlank(Map<String, String> map, String key, String value) {
        if (value != null && !value.isBlank()) {
            map.put(key, value);
        }
    }

    private String formatModifier(Integer value) {
        if (value == null) return null;
        return value >= 0 ? "+" + value : String.valueOf(value);
    }

    private String str(Integer value) {
        return value != null ? String.valueOf(value) : null;
    }
}
```

- [ ] **Step 13: Run CharacterFieldMapper tests**

Run: `bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn" test -pl services/character-service/character-service-adapter-inbound -Dtest=CharacterFieldMapperTest -f /Users/marybookpro/IdeaProjects/DnDSupportProject/pom.xml`

Expected: All 17 tests PASS.

- [ ] **Step 14: Commit**

```bash
git add services/character-service/character-service-adapter-inbound/src/main/java/com/dndplatform/character/adapter/inbound/create/generator/CharacterFieldMap.java \
      services/character-service/character-service-adapter-inbound/src/main/java/com/dndplatform/character/adapter/inbound/create/generator/CharacterFieldMapper.java \
      services/character-service/character-service-adapter-inbound/src/test/java/com/dndplatform/character/adapter/inbound/create/generator/CharacterFieldMapperTest.java
git commit -m "feat: extract CharacterFieldMapper with tests"
```

---

### Task 6: Refactor PdfCharacterSheetGenerator to use CharacterFieldMapper

**Files:**
- Modify: `services/character-service/character-service-adapter-inbound/src/main/java/com/dndplatform/character/adapter/inbound/create/generator/PdfCharacterSheetGenerator.java`

- [ ] **Step 15: Replace PdfCharacterSheetGenerator with thin version using mapper**

Replace the entire file with:

```java
package com.dndplatform.character.adapter.inbound.create.generator;

import com.dndplatform.character.domain.CharacterSheetGenerator;
import com.dndplatform.character.domain.model.Character;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class PdfCharacterSheetGenerator implements CharacterSheetGenerator {

    private static final Logger log = Logger.getLogger(PdfCharacterSheetGenerator.class.getName());

    private static final String TEMPLATE_PATH = "META-INF/resources/wotc-5e-sheet.pdf";

    private final CharacterFieldMapper fieldMapper;

    @Inject
    public PdfCharacterSheetGenerator(CharacterFieldMapper fieldMapper) {
        this.fieldMapper = fieldMapper;
    }

    @Override
    public byte[] generate(Character character) {
        byte[] templateBytes = loadTemplate();
        CharacterFieldMap fieldMap = fieldMapper.mapFields(character);

        try (PDDocument document = Loader.loadPDF(templateBytes)) {
            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
            if (acroForm == null) {
                throw new IOException("PDF template does not contain an AcroForm");
            }

            Map<String, PDField> pdfFieldMap = buildFieldMap(acroForm);
            acroForm.setNeedAppearances(false);

            for (Map.Entry<String, String> entry : fieldMap.textFields().entrySet()) {
                setTextField(pdfFieldMap, entry.getKey(), entry.getValue());
            }

            for (Map.Entry<String, Boolean> entry : fieldMap.checkboxFields().entrySet()) {
                setCheckBox(pdfFieldMap, entry.getKey(), entry.getValue());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate character sheet PDF", e);
        }
    }

    byte[] loadTemplate() {
        try (InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(TEMPLATE_PATH)) {
            if (is == null) {
                throw new RuntimeException("PDF template not found on classpath: " + TEMPLATE_PATH);
            }
            return is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load PDF template", e);
        }
    }

    private Map<String, PDField> buildFieldMap(PDAcroForm acroForm) {
        Map<String, PDField> map = new HashMap<>();
        for (PDField field : acroForm.getFieldTree()) {
            String name = field.getFullyQualifiedName();
            map.put(name, field);
            String trimmed = name.trim();
            if (!trimmed.equals(name)) {
                map.putIfAbsent(trimmed, field);
            }
        }
        return map;
    }

    private void setTextField(Map<String, PDField> fieldMap, String name, String value) {
        if (value == null || value.isBlank()) return;

        PDField field = fieldMap.get(name);
        if (field == null) {
            log.fine(() -> "PDF field not found: '%s'".formatted(name));
            return;
        }

        try {
            field.setValue(value);
        } catch (IOException e) {
            log.warning(() -> "Failed to set PDF field '%s': %s".formatted(name, e.getMessage()));
        }
    }

    private void setCheckBox(Map<String, PDField> fieldMap, String name, boolean checked) {
        PDField field = fieldMap.get(name);
        if (field == null) {
            log.fine(() -> "PDF checkbox not found: '%s'".formatted(name));
            return;
        }

        if (field instanceof PDCheckBox checkbox) {
            try {
                if (checked) {
                    checkbox.check();
                } else {
                    checkbox.unCheck();
                }
            } catch (IOException e) {
                log.warning(() -> "Failed to set checkbox '%s': %s".formatted(name, e.getMessage()));
            }
        }
    }
}
```

- [ ] **Step 16: Run all character-service adapter-inbound tests**

Run: `bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn" test -pl services/character-service/character-service-adapter-inbound -f /Users/marybookpro/IdeaProjects/DnDSupportProject/pom.xml`

Expected: All existing tests PASS. `CharacterCreateDelegateTest` still passes because `PdfCharacterSheetGenerator` is mocked behind `CharacterSheetGenerator` interface.

- [ ] **Step 17: Commit**

```bash
git add services/character-service/character-service-adapter-inbound/src/main/java/com/dndplatform/character/adapter/inbound/create/generator/PdfCharacterSheetGenerator.java
git commit -m "refactor: thin PdfCharacterSheetGenerator, delegate field mapping to CharacterFieldMapper"
```

---

### Task 7: Write PdfCharacterSheetGenerator integration test

**Files:**
- Create: `services/character-service/character-service-adapter-inbound/src/test/java/com/dndplatform/character/adapter/inbound/create/generator/PdfCharacterSheetGeneratorTest.java`

The PDF template is at `src/main/resources/META-INF/resources/wotc-5e-sheet.pdf`. Maven automatically includes `src/main/resources` on test classpath, so `loadTemplate()` will find it.

- [ ] **Step 18: Write the integration test**

```java
package com.dndplatform.character.adapter.inbound.create.generator;

import com.dndplatform.character.domain.model.*;
import com.dndplatform.character.domain.model.Character;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PdfCharacterSheetGeneratorTest {

    private PdfCharacterSheetGenerator sut;

    @BeforeEach
    void setUp() {
        sut = new PdfCharacterSheetGenerator(new CharacterFieldMapper());
    }

    @Test
    void shouldGenerateValidPdfWithCharacterData() throws IOException {
        Character character = new CharacterBuilder()
                .withName("Gandalf")
                .withSpecies("Human")
                .withCharacterClass("Wizard")
                .withLevel(20)
                .withBackground("Sage")
                .withAlignment("Neutral Good")
                .withExperiencePoints(355000)
                .withProficiencyBonus(6)
                .withArmorClass(15)
                .withSpeed(30)
                .withHitPointsMax(102)
                .withHitPointsCurrent(98)
                .withHitDiceTotal(20)
                .withHitDiceType("d6")
                .withAbilityScores(new AbilityScoresBuilder()
                        .withStrength(10).withDexterity(14).withConstitution(14)
                        .withIntelligence(20).withWisdom(16).withCharisma(12)
                        .build())
                .withPersonalityTraits("Curious and wise")
                .withIdeals("Knowledge is power")
                .withBonds("The fellowship")
                .withFlaws("Overly cryptic")
                .build();

        byte[] pdfBytes = sut.generate(character);

        assertThat(pdfBytes).isNotEmpty();

        try (PDDocument doc = Loader.loadPDF(pdfBytes)) {
            PDAcroForm form = doc.getDocumentCatalog().getAcroForm();
            assertThat(form).isNotNull();

            assertThat(fieldValue(form, "CharacterName")).isEqualTo("Gandalf");
            assertThat(fieldValue(form, "Race")).isEqualTo("Human");
            assertThat(fieldValue(form, "ClassLevel")).isEqualTo("Wizard 20");
            assertThat(fieldValue(form, "Background")).isEqualTo("Sage");
            assertThat(fieldValue(form, "Alignment")).isEqualTo("Neutral Good");
            assertThat(fieldValue(form, "STR")).isEqualTo("10");
            assertThat(fieldValue(form, "INT")).isEqualTo("20");
            assertThat(fieldValue(form, "INTmod")).isEqualTo("+5");
            assertThat(fieldValue(form, "AC")).isEqualTo("15");
            assertThat(fieldValue(form, "HPMax")).isEqualTo("102");
            assertThat(fieldValue(form, "PersonalityTraits")).isEqualTo("Curious and wise");
        }
    }

    @Test
    void shouldGeneratePdfWithSkillsAndSavingThrows() throws IOException {
        Character character = new CharacterBuilder()
                .withName("TestChar")
                .withSkills(List.of(
                        new SkillProficiencyBuilder().withName("Arcana").withProficient(true).withModifier(9).build(),
                        new SkillProficiencyBuilder().withName("Perception").withProficient(true).withModifier(6).build()
                ))
                .withSavingThrows(List.of(
                        new SavingThrowProficiencyBuilder().withAbility("INT").withProficient(true).withModifier(9).build(),
                        new SavingThrowProficiencyBuilder().withAbility("WIS").withProficient(true).withModifier(6).build()
                ))
                .build();

        byte[] pdfBytes = sut.generate(character);

        try (PDDocument doc = Loader.loadPDF(pdfBytes)) {
            PDAcroForm form = doc.getDocumentCatalog().getAcroForm();

            assertThat(fieldValue(form, "Arcana")).isEqualTo("+9");
            assertThat(fieldValue(form, "Perception")).isEqualTo("+6");
            assertThat(fieldValue(form, "ST Intelligence")).isEqualTo("+9");
            assertThat(fieldValue(form, "ST Wisdom")).isEqualTo("+6");

            assertThat(isChecked(form, "Check Box 25")).isTrue();  // Arcana
            assertThat(isChecked(form, "Check Box 14")).isTrue();  // INT save
            assertThat(isChecked(form, "Check Box 15")).isTrue();  // WIS save
        }
    }

    @Test
    void shouldGeneratePdfWithEquipmentAndSpellSlots() throws IOException {
        Character character = new CharacterBuilder()
                .withName("TestChar")
                .withEquipment(List.of(
                        new EquipmentBuilder().withName("Staff").withQuantity(1).build(),
                        new EquipmentBuilder().withName("Potion").withQuantity(5).build()
                ))
                .withSpellSlots(List.of(
                        new SpellSlotAllocationBuilder().withSpellLevel(1).withSlotsTotal(4).build(),
                        new SpellSlotAllocationBuilder().withSpellLevel(3).withSlotsTotal(2).build()
                ))
                .build();

        byte[] pdfBytes = sut.generate(character);

        try (PDDocument doc = Loader.loadPDF(pdfBytes)) {
            PDAcroForm form = doc.getDocumentCatalog().getAcroForm();

            assertThat(fieldValue(form, "Equipment")).isEqualTo("Staff\nPotion (x5)");
            assertThat(fieldValue(form, "SlotsTotal 19")).isEqualTo("4");
            assertThat(fieldValue(form, "SlotsTotal 21")).isEqualTo("2");
        }
    }

    @Test
    void shouldGeneratePdfWithMinimalData() throws IOException {
        Character character = new CharacterBuilder()
                .withName("Minimal")
                .build();

        byte[] pdfBytes = sut.generate(character);

        assertThat(pdfBytes).isNotEmpty();
        try (PDDocument doc = Loader.loadPDF(pdfBytes)) {
            PDAcroForm form = doc.getDocumentCatalog().getAcroForm();
            assertThat(fieldValue(form, "CharacterName")).isEqualTo("Minimal");
        }
    }

    @Test
    void shouldGeneratePassivePerceptionFromSkills() throws IOException {
        Character character = new CharacterBuilder()
                .withName("Perceptive")
                .withSkills(List.of(
                        new SkillProficiencyBuilder().withName("Perception").withProficient(true).withModifier(5).build()
                ))
                .build();

        byte[] pdfBytes = sut.generate(character);

        try (PDDocument doc = Loader.loadPDF(pdfBytes)) {
            PDAcroForm form = doc.getDocumentCatalog().getAcroForm();
            assertThat(fieldValue(form, "Passive")).isEqualTo("15");
        }
    }

    private String fieldValue(PDAcroForm form, String fieldName) {
        PDField field = form.getField(fieldName);
        if (field == null) {
            // Try with trailing space (WotC PDF quirk)
            for (PDField f : form.getFieldTree()) {
                if (f.getFullyQualifiedName().trim().equals(fieldName)) {
                    return f.getValueAsString();
                }
            }
            return null;
        }
        return field.getValueAsString();
    }

    private boolean isChecked(PDAcroForm form, String fieldName) {
        PDField field = form.getField(fieldName);
        if (field instanceof PDCheckBox checkbox) {
            return checkbox.isChecked();
        }
        return false;
    }
}
```

- [ ] **Step 19: Run integration test**

Run: `bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn" test -pl services/character-service/character-service-adapter-inbound -Dtest=PdfCharacterSheetGeneratorTest -f /Users/marybookpro/IdeaProjects/DnDSupportProject/pom.xml`

Expected: All 5 tests PASS.

- [ ] **Step 20: Commit**

```bash
git add services/character-service/character-service-adapter-inbound/src/test/java/com/dndplatform/character/adapter/inbound/create/generator/PdfCharacterSheetGeneratorTest.java
git commit -m "test: add PdfCharacterSheetGenerator integration tests"
```

---

### Task 8: Run full test suite for both services

- [ ] **Step 21: Run chat-service full test suite**

Run: `bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn" test -pl services/chat-service/chat-service-adapter-inbound -f /Users/marybookpro/IdeaProjects/DnDSupportProject/pom.xml`

Expected: All tests PASS.

- [ ] **Step 22: Run character-service full test suite**

Run: `bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn" test -pl services/character-service/character-service-adapter-inbound -f /Users/marybookpro/IdeaProjects/DnDSupportProject/pom.xml`

Expected: All tests PASS.

- [ ] **Step 23: Final commit if any fixups needed, otherwise done**
